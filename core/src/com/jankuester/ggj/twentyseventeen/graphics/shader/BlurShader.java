package com.jankuester.ggj.twentyseventeen.graphics.shader;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.g3d.utils.RenderContext;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class BlurShader implements Shader {

    private ShaderProgram blurShaderProgram;
    private RenderContext context;
    private Camera camera;

    public static final String UNIFORM_DIR = "dir";
    public static final String UNIFORM_RESOLUTION = "resolution";
    public static final String UNIFORM_RADIUS = "radius";

    public static final Vector2 dirVec = new Vector2(1.0f, 0.0f);
    public static int resolution = 2056;
    public static float radius = 3.0f;

    @Override
    public void init() {
	this.blurShaderProgram = createBlurShaderProgram(dirVec, resolution, radius);
    }

    public static ShaderProgram createBlurShaderProgram(Vector2 dirVec, int resolution, float radius) {
	String vert = Gdx.files.internal("shaders/blur/blur.vert").readString();
	String frag = Gdx.files.internal("shaders/blur/blur.frag").readString();
	ShaderProgram blurShaderProgram = new ShaderProgram(vert, frag);
	if (!blurShaderProgram.isCompiled())
	    throw new GdxRuntimeException(blurShaderProgram.getLog());

	blurShaderProgram.setUniformf(UNIFORM_DIR, dirVec);
	blurShaderProgram.setUniformf(UNIFORM_RESOLUTION, resolution);
	blurShaderProgram.setUniformf(UNIFORM_RADIUS, radius);
	return blurShaderProgram;
    }

    public void setDir(float x, float y) {
	dirVec.set(x, y);
	blurShaderProgram.setUniformf(UNIFORM_DIR, dirVec);
    }

    public void setResolution(int resolution) {
	this.resolution = resolution;
    }

    public void setRadius(float radius) {
	this.radius = radius;
    }

    @Override
    public void dispose() {
	blurShaderProgram.dispose();
    }

    @Override
    public void begin(Camera camera, RenderContext context) {
	this.camera = camera;
	this.context = context;
	blurShaderProgram.begin();
    }

    @Override
    public void render(Renderable renderable) {

    }

    @Override
    public void end() {
	blurShaderProgram.end();
    }

    @Override
    public int compareTo(Shader other) {
	return 0;
    }

    @Override
    public boolean canRender(Renderable instance) {
	return true;
    }
}
