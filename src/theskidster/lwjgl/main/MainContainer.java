package theskidster.lwjgl.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.FloatBuffer;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import static org.lwjgl.glfw.GLFW.*;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.system.MemoryUtil.NULL;
import theskidster.lwjgl.graphics.BitmapFont;
import theskidster.lwjgl.graphics.Quad;
import theskidster.lwjgl.shader.Shader;
import theskidster.lwjgl.graphics.Texture;

/**
 * @author J Hoffman
 * Created: Dec 26, 2018
 */

public class MainContainer implements Runnable {

    private int scale = 4;
    private final int WIDTH = 320;
    private final int HEIGHT = 224;
    private int prog;
    
    private long context;
    
    public static FloatBuffer fbModel = BufferUtils.createFloatBuffer(16);
    public static FloatBuffer fbView = BufferUtils.createFloatBuffer(16);
    public static FloatBuffer fbProj = BufferUtils.createFloatBuffer(16);
    public static FloatBuffer fbSprite = BufferUtils.createFloatBuffer(2);
    
    private BitmapFont bgf;
    private String text = "press 1, 2, or 3 to change this text.";
    
    public MainContainer() {
        if(!glfwInit()) throw new RuntimeException("Failed to initialize glfw.");
        
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_VISIBLE, GLFW_TRUE);
        
        context = glfwCreateWindow(WIDTH * scale, HEIGHT * scale, "LWJGL3: text wranglin", NULL, NULL);
        GLFWVidMode vm = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(context, (vm.width() - WIDTH * scale) / 2, (vm.height() - HEIGHT * scale) / 2);
        
        glfwSetKeyCallback(context, (long window, int key, int scanCode, int action, int mods) -> {
            if(key == GLFW_KEY_ESCAPE) glfwSetWindowShouldClose(context, true);
            
            if(key == GLFW_KEY_1) text = "who needs multicam when you've got style?";
            if(key == GLFW_KEY_2) text = "dingos ate mah baby!";
            if(key == GLFW_KEY_3) text = "i think it\'s gonna rain, when i die.";
            
            if(key == GLFW_KEY_UP) bgf.position.y--;
            if(key == GLFW_KEY_DOWN) bgf.position.y++;
            if(key == GLFW_KEY_LEFT) bgf.position.x--;
            if(key == GLFW_KEY_RIGHT) bgf.position.x++;
        });
        
        glfwMakeContextCurrent(context);
        glfwSwapInterval(1);
        glfwShowWindow(context);
        GL.createCapabilities();
        
        init();
    }
    
    /**
     * This is temporary, in a state machine this would be set up before drawing,
     * I just wanted to keep things clean.
     */
    public void init() {
        Shader vs = loadShader("ShaderVertex.glsl", GL_VERTEX_SHADER);
        Shader fs = loadShader("ShaderFragment.glsl", GL_FRAGMENT_SHADER);
        
        prog = glCreateProgram();
        glAttachShader(prog, vs.id);
        glAttachShader(prog, fs.id);
        glLinkProgram(prog);
        glUseProgram(prog);
        
        Shader.uniforms.put("model", glGetUniformLocation(prog, "uModel"));
        Shader.uniforms.put("view", glGetUniformLocation(prog, "uView"));
        Shader.uniforms.put("projection", glGetUniformLocation(prog, "uProj"));
        Shader.uniforms.put("tex", glGetUniformLocation(prog, "uTexOffset"));
        
        int vao = glGenVertexArrays();
        glBindVertexArray(vao);
        
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }
    
    @Override
    public void run() {
        int errCode;
        
        glUniformMatrix4fv(Shader.uniforms.get("projection"), false, new Matrix4f()
                .ortho(0, WIDTH, HEIGHT, 0, -1, 10)
                .get(fbProj)
        );
        
        bgf = new BitmapFont(
                new Vector3f(10.0f, 10.0f, 0.0f), 
                new Vector2f(0.0f, 0.0f),
                new Quad(6, 6),
                new Texture("bgf_system.png", 96, 24)
        );
        
        while(!glfwWindowShouldClose(context)) {
            glClear(GL_COLOR_BUFFER_BIT);
            glClearColor((91f / 255f), (122f / 255f), (239f / 255f), 0);
            
            glUniformMatrix4fv(Shader.uniforms.get("view"), false, new Matrix4f()
                    .get(fbView)
            );
            
            bgf.drawText(text, bgf.position);
            
            glfwSwapBuffers(context);
            glfwPollEvents();
            
            errCode = glGetError();
            if(errCode != GL_NO_ERROR) throw new RuntimeException("OPENGL ERROR: " + errCode);
        }
        glfwTerminate();
    }
    
    /**
     * At some point here I'm going to need to make a MainResource manager class
     * or something, ideally this would be static.
     * 
     * @param fileName  - filename of the file we want parsed
     * @param type      - type of shader to compile
     * @return          - shader object
     */
    private Shader loadShader(String fileName, int type) {
        StringBuilder sb = new StringBuilder();
        
        try(InputStream in = getClass().getResourceAsStream("/theskidster/lwjgl/shader/" + fileName); BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
            String line;
            while((line = br.readLine()) != null) sb.append(line).append("\n");
        } catch(IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to parse GLSL file.");
        }
        
        CharSequence src = sb.toString();
        return new Shader(type, src);
    }
    
}