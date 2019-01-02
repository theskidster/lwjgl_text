package theskidster.lwjgl.graphics;

import java.util.HashMap;
import java.util.Map;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import static org.lwjgl.opengl.GL20.glUniform2fv;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import theskidster.lwjgl.entity.Entity;
import theskidster.lwjgl.main.MainContainer;
import theskidster.lwjgl.shader.Shader;

/**
 * @author J Hoffman
 * Created: Dec 29, 2018
 */

public class BitmapFont extends Entity {
    
    private float charXPos = 0.0f;
    private float charYPos = 0.0f;
    
    private String charSet =    " !\"#$%&\'()*+,-./" + "\n" +
                                "0123456789:;<=>?" + "\n" +
                                "@abcdefghijklmno" + "\n" +
                                "pqrstuvwxyz[\\]^_";
    
    private Map<Character, Vector2f> glyphs = new HashMap();
    
    public BitmapFont(Vector3f position, Vector2f offset, Quad quad, Texture tex) {
        super(position, offset, quad, tex);
        
        for(char glyph : charSet.toCharArray()) {
            if(glyph != '\n') {
                glyphs.put(glyph, new Vector2f(charXPos, charYPos));
                charXPos += this.sprites.cellWidth;
            } else {
                charXPos = 0.0f;
                charYPos += this.sprites.cellHeight;
            }
        }
    }
    
    /**
     * This method will iterate through each character in the string and display 
     * the corresponding sprite. Because java provide the built-in functionality 
     * to find the current index of the for loop I had to make my own.
     * 
     * @param text      - text to be drawn
     * @param position  - desired position where the text will start
     */
    public void drawText(String text, Vector3f position) {
        int index = 0;
        for(char glyph : text.toCharArray()) {
            
            glUniformMatrix4fv(Shader.uniforms.get("model"), false, new Matrix4f()
                    .translate(position.x + this.quad.width * index, position.y, position.z)
                    .get(MainContainer.fbModel)
            );

            glUniform2fv(Shader.uniforms.get("tex"), new Vector2f()
                    .set(glyphs.get(glyph).x, glyphs.get(glyph).y)
                    .get(MainContainer.fbSprite)
            );

            this.renderInstance(text.length());
            index++;
        }
    }
    
}