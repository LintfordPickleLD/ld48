package net.ruse.ld48.renderers;

import net.lintford.library.core.LintfordCore;
import net.lintford.library.core.ResourceManager;
import net.lintford.library.core.graphics.ColorConstants;
import net.lintford.library.core.graphics.textures.Texture;
import net.lintford.library.renderers.BaseRenderer;
import net.lintford.library.renderers.RendererManager;
import net.ruse.ld48.controllers.CameraZoomController;
import net.ruse.ld48.controllers.GameStateController;
import net.ruse.ld48.controllers.LevelController;
import net.ruse.ld48.controllers.PlayerController;

public class HudRenderer extends BaseRenderer {

	// --------------------------------------
	// Constants
	// --------------------------------------

	public static final String RENDERER_NAME = "Hud Renderer";

	// --------------------------------------
	// Variables
	// --------------------------------------

	private CameraZoomController mCameraZoomController;
	private GameStateController mGameStateController;
	private PlayerController mPlayerController;
	private LevelController mLevelController;
	private Texture mHudTexture;

	// --------------------------------------
	// Properties
	// --------------------------------------

	@Override
	public boolean isInitialized() {
		return mLevelController != null;
	}

	// --------------------------------------
	// Constructor
	// --------------------------------------

	public HudRenderer(RendererManager pRendererManager, int pEntityGroupID) {
		super(pRendererManager, RENDERER_NAME, pEntityGroupID);

	}

	// --------------------------------------
	// Core-Methods
	// --------------------------------------

	@Override
	public void initialize(LintfordCore pCore) {
		mLevelController = (LevelController) pCore.controllerManager().getControllerByNameRequired(LevelController.CONTROLLER_NAME, entityGroupID());
		mGameStateController = (GameStateController) pCore.controllerManager().getControllerByNameRequired(GameStateController.CONTROLLER_NAME, entityGroupID());
		mPlayerController = (PlayerController) pCore.controllerManager().getControllerByNameRequired(PlayerController.CONTROLLER_NAME, entityGroupID());
		mCameraZoomController = (CameraZoomController) pCore.controllerManager().getControllerByNameRequired(CameraZoomController.CONTROLLER_NAME, entityGroupID());
	}

	@Override
	public void loadGLContent(ResourceManager pResourceManager) {
		super.loadGLContent(pResourceManager);

		mHudTexture = pResourceManager.textureManager().loadTexture("TEXTURE_HUD", "res/textures/textureHud.png", entityGroupID());

	}

	@Override
	public void draw(LintfordCore pCore) {

		final float lTopWindowPadding = 24.f;

		final var lHudRect = pCore.HUD().boundingRectangle();
		final var lLevel = mLevelController.level();

		if (lLevel == null)
			return;

		final float lTargetZoomFactor = mCameraZoomController.targetCameraZoom();

		final var lFontUnit = rendererManager().titleFont();
		final var lTextureBatch = rendererManager().uiTextureBatch();

		final int lFullHeartCount = 4;
		final int lPlayerHealth = mPlayerController.playerMobInstance().health;

		lTextureBatch.begin(pCore.HUD());
		lTextureBatch.draw(mHudTexture, 0, 32, 32, 32, lHudRect.left() + 32, lHudRect.top() + lTopWindowPadding, 32.f * (lTargetZoomFactor), 32.f * (lTargetZoomFactor), -0.1f, ColorConstants.WHITE);

		for (int i = 0; i < lFullHeartCount; i++) {
			final float lHeartPositionX = lHudRect.right() - (lFullHeartCount * 24.f * lTargetZoomFactor) - 32.f + (i * 24.f * lTargetZoomFactor);
			final float lHeartPositionY = lHudRect.top() + lTopWindowPadding;

			if (lPlayerHealth > i) {
				lTextureBatch.draw(mHudTexture, 32, 32, 32, 32, lHeartPositionX, lHeartPositionY, 32.f * lTargetZoomFactor, 32.f * lTargetZoomFactor, -0.1f, ColorConstants.WHITE);

			} else {
				lTextureBatch.draw(mHudTexture, 32, 32, 32, 32, lHeartPositionX, lHeartPositionY, 32.f * lTargetZoomFactor, 32.f * lTargetZoomFactor, -0.1f, ColorConstants.getBlackWithAlpha(.5f));

			}

		}

		lTextureBatch.end();

		lFontUnit.begin(pCore.HUD());
		lFontUnit.draw(mGameStateController.currentGold() + " / " + mGameStateController.targetGold(), lHudRect.left() + 60 * lTargetZoomFactor, lHudRect.top() + lTopWindowPadding + 5, lTargetZoomFactor);
		lFontUnit.end();

	}
}