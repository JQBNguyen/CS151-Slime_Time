package entity;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.shape.Shape;
import main.GameApplication;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Entity {
    protected GameApplication ga;

    public int worldX, worldY; // World Coordinates
    public int speed; // Speed of Entity (# of Pixels Walked per Tick)

    //Character Attributes
    public int maxLife;
    public int life;

    //Item attributes
    public int attackValue;
    public int attackSpeed;
    public List<Image> images = new ArrayList<>();
    public String direction = "down"; // Direction Entity is Moving, direction has a default setting (down)
    public int spriteCounter = 0; // Animation Timer
    public int spriteNum = 1; // Sprite Frame
    public Shape solidArea; // Hit box
    public int solidAreaDefaultX, solidAreaDefaultY; // Hit box Coordinates
    public boolean collision = false; // Collision State
    public boolean collisionOn;
    public String name;

    public int iFrameCount;

    public Entity(GameApplication ga) {
        this.ga = ga;
    }
    public Entity(){}

    //setting up images and making sure that the character properly sees valid entities
    public void render(GraphicsContext gc, GameApplication ga) {
        int screenX = worldX - ga.player.worldX + ga.player.screenX;
        int screenY = worldY - ga.player.worldY + ga.player.screenY;

        // Draws Only What Camera Can See
        if (worldX + ga.TILE_SIZE > ga.player.worldX - ga.player.screenX &&
                worldX - ga.TILE_SIZE  < ga.player.worldX + ga.player.screenX &&
                worldY + ga.TILE_SIZE  > ga.player.worldY - ga.player.screenY &&
                worldY - ga.TILE_SIZE  < ga.player.worldY + ga.player.screenY) {
            gc.drawImage(images.get(-1 + spriteNum), screenX, screenY);
        }
    }

    public void setup(String imageName, String fileType, int width, int height) {
        try {
            images.add(new Image(new FileInputStream("Slime_Time/res/" + fileType + "/" + imageName + ".png"), width, height, false, false));
        }
        catch (Exception e) {
            try {
                images.add(new Image(new FileInputStream("Slime_Time/res/tiles/no_sprite.png"), width, height, false, false));
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
