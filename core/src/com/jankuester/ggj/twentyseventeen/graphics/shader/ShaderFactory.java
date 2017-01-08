package com.jankuester.ggj.twentyseventeen.graphics.shader;

import com.badlogic.gdx.graphics.g3d.Shader;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;

public class ShaderFactory {
    public static Shader getBlurShader() {
	Shader blurShader = new BlurShader();
	blurShader.init();
	return blurShader;
    }

    public static ShaderProgram getBlurShaderprogram(Vector2 dirVec, int resolution, float radius) {
	return BlurShader.createBlurShaderProgram(dirVec, resolution, radius);
    }

    public static ShaderProgram getBlurShaderprogram() {
	return BlurShader.createBlurShaderProgram(BlurShader.dirVec, BlurShader.resolution, BlurShader.radius);
    }

}
