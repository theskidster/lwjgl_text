package theskidster.lwjgl.entity;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL31.*;
import theskidster.lwjgl.graphics.Quad;
import theskidster.lwjgl.graphics.SpriteSheet;
import theskidster.lwjgl.graphics.Texture;
import theskidster.lwjgl.main.MainContainer;
import theskidster.lwjgl.shader.Shader;

/**
 * @author J Hoffman
 * Created: Dec 26, 2018
 */

public class Entity {
    
    private int vbo;
    private int ibo;
    
    private float vertices[];
    private int indices[];
    
    public Vector3f position;
    public Vector2f offset;
    public Quad quad;
    public Texture tex;
    public SpriteSheet sprites;
    
    public Entity(Vector3f position, Vector2f offset, Quad quad, Texture tex) {
        this.position = position;
        this.offset = offset;
        this.quad = quad;
        this.tex = tex;
        this.sprites = new SpriteSheet(this.quad, this.tex);
        
        this.vertices = new float[] {
            //position                                      //texCoords
            -(quad.width / 2), -(quad.height / 2), 0.0f,    0.0f, 0.0f,
            -(quad.width / 2),  (quad.height / 2), 0.0f,    0.0f, sprites.cellHeight,
             (quad.width / 2),  (quad.height / 2), 0.0f,    sprites.cellWidth, sprites.cellHeight,
             (quad.width / 2), -(quad.height / 2), 0.0f,    sprites.cellWidth, 0.0f
        };
        
        this.indices = new int[] {
            0, 1, 2,
            2, 3, 0
        };
        
        this.vbo = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vbo);
        glBufferData(GL_ARRAY_BUFFER, this.vertices, GL_STATIC_DRAW);
        
        this.ibo = glGenBuffers();
        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, ibo);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, this.indices, GL_STATIC_DRAW);
        
        glVertexAttribPointer(0, 3, GL_FLOAT, false, (5 * Float.BYTES), 0);
        glVertexAttribPointer(1, 2, GL_FLOAT, false, (5 * Float.BYTES), (3 * Float.BYTES));
    }
    
    /**
     * manages position and current frame of the sprite sheet, renders the entity.
     */
    public void render() {
        glUniformMatrix4fv(Shader.uniforms.get("model"), false, new Matrix4f()
                .translate(this.position)
                .get(MainContainer.fbModel)
        );

        glUniform2fv(Shader.uniforms.get("tex"), new Vector2f()
                .set(this.offset.x, this.offset.y)
                .get(MainContainer.fbSprite)
        );
        
        glDrawElements(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0);
    }
    
    /**
     * renders an instance of the entity.
     * 
     * @param numToDraw - number of entities to draw, may need to be multiplied by the number of vertex attributes
     */
    public void renderInstance(int numToDraw) {
        glDrawElementsInstanced(GL_TRIANGLES, this.indices.length, GL_UNSIGNED_INT, 0, numToDraw);
    }
    
}