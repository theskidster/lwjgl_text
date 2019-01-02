package theskidster.lwjgl.graphics;

/**
 * @author J Hoffman
 * Created: Dec 26, 2018
 */

public class SpriteSheet {
    
    public float[][] cells;
    public float cellWidth;
    public float cellHeight;
    
    public Texture tex;
    public Quad quad;
    
    public SpriteSheet(Quad quad, Texture tex) {
        this.tex = tex;
        this.quad = quad;
        this.cellWidth = (float) quad.width / tex.width;
        this.cellHeight = (float) quad.height / tex.height;
        
        cells = new float[(int) (tex.width / quad.width)][(int) (tex.height / quad.height)];
    }
    
}