package net.goo.brutality.util.particle;

import net.goo.brutality.Brutality;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL43;

public class BrutalityShaders {
    private static Shader CATMULL_ROM;
    private static ShaderProgram CATMULL_ROM_PROGRAM;

    public static void init() {
        CATMULL_ROM = Shaders.load(Shader.ShaderType.COMPUTE, ResourceLocation.fromNamespaceAndPath(Brutality.MOD_ID, "catmull_rom"));
    }

    public static ShaderProgram getCatmullRomProgram() {
        if (CATMULL_ROM_PROGRAM == null) {
            CATMULL_ROM_PROGRAM = new ShaderProgram();
            CATMULL_ROM_PROGRAM.attach(CATMULL_ROM);
        }
        return CATMULL_ROM_PROGRAM;
    }

    public static int loadComputeShader(String code) {
        int shader = GL43.glCreateShader(GL43.GL_COMPUTE_SHADER);
        GL43.glShaderSource(shader, code);
        GL43.glCompileShader(shader);

        if (GL43.glGetShaderi(shader, GL43.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Compute shader failed to compile: " + GL43.glGetShaderInfoLog(shader));
        }

        int program = GL43.glCreateProgram();
        GL43.glAttachShader(program, shader);
        GL43.glLinkProgram(program);

        if (GL43.glGetProgrami(program, GL43.GL_LINK_STATUS) == GL11.GL_FALSE) {
            throw new RuntimeException("Compute program failed to link: " + GL43.glGetProgramInfoLog(program));
        }

        GL43.glDeleteShader(shader); // Clean up intermediate shader object
        return program;
    }
}
