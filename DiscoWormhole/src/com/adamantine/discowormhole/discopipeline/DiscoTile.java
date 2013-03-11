/* Author: Christopher Grabowski */

package com.adamantine.discowormhole.discopipeline;

import com.adamantine.texture.TextureManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g3d.loaders.g3d.G3dLoader;
import com.badlogic.gdx.graphics.g3d.materials.Material;
import com.badlogic.gdx.graphics.g3d.materials.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.model.still.StillModel;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Matrix4;

public class DiscoTile implements RingSection {
	static final float[] DEFAULT_LIGHT_POS = new float[] { 0.0f, 0.0f, -20f };

	private final Matrix4 model;
	private final ShaderProgram shader;
	private final Matrix4 modelViewProjection;
	private final Matrix4 modelView;
	private final Matrix4 normalMatrix;
	private final StillModel tileModel;
	private final Texture texture;
	private final Texture normals;
	private final Material material;
	private final FileHandle handle;

	// private ShapeRenderer shapes;

	public DiscoTile() {
		// G3dtToG3dConverter.convert("models/space_tile3.g3dt",
		// "C:\\workspace\\space_tile.g3d");

		handle = Gdx.files.internal("models/space_tile3.g3d");
		texture = new Texture(Gdx.files.internal("textures/glitter1.jpg"));
		texture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		TextureManager.getInstance().bind(texture, 0);
		normals = new Texture(Gdx.files.internal("textures/glitter2_n.jpg"));
		TextureManager.getInstance().bind(normals, 1);
		material = new Material("material", new TextureAttribute(texture, 0,
				"u_texture"), new TextureAttribute(normals, 1, "u_normals"));
		tileModel = G3dLoader.loadStillModel(handle);
		tileModel.setMaterial(material);

		shader = new ShaderProgram(Gdx.files.internal("shaders/tile.vsh"),
				Gdx.files.internal("shaders/tile.fsh"));
		if (shader.isCompiled() == false) {
			Gdx.app.log("ShaderTest", shader.getLog());
			System.exit(1);
		}

		model = new Matrix4();
		modelView = new Matrix4();
		normalMatrix = new Matrix4();

		modelViewProjection = new Matrix4();
	}

	public void createModel() {

	}

	@Override
	public void render(Camera camera) {
		render(camera, DEFAULT_LIGHT_POS);
	}

	@Override
	public void render(Camera camera, float[] lightsPos) {
		this.render(camera, lightsPos, 1.0f, 1.0f, 1.0f, 1.0f);
	}

	@Override
	public void render(Camera camera, float[] lightsPos, float red,
			float green, float blue, float alpha) {

		model.rotate(0.0f, 1.0f, 0.0f, 90.0f);

		modelViewProjection.set(camera.combined).mul(model);
		modelView.set(camera.view).mul(model);
		normalMatrix.set(camera.view).mul(model);

		try {
			shader.begin();
			ShaderProgram.pedantic = true;
			shader.setUniform3fv("u_lightsPos", lightsPos, 0, 6);
			shader.setUniformf("u_color", red, green, blue, 1.0f);
			shader.setUniformMatrix("u_modelView", modelView);
			shader.setUniformMatrix("u_modelViewProjection",
					modelViewProjection);
			shader.setUniformMatrix("u_normalMatrix", normalMatrix);
			tileModel.render(shader);
			shader.end();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public Matrix4 getModelMatrix() {
		return model;
	}

	@Override
	public void dispose() {
		tileModel.dispose();
		texture.dispose();
		normals.dispose();
		shader.dispose();
	}
}
