package entity;

import Combat.Scythe;
import Combat.Slingshot;
import javafx.scene.shape.Rectangle;
import main.GameApplication;
import main.KeyHandler;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import object.OBJ_Gold;
import object.OBJ_Stone;
import object.OBJ_Wood;
import object.SuperObject;
import java.util.ArrayList;
import java.util.List;

public class Player extends Entity{
    public KeyHandler keyH; // Key Handler to Deal with Movement and Potential other Key Presses
    public final int screenX; // Screen X-Coord
    public final int screenY; // Screen Y-Coord
    public Scythe scythe;
    public Slingshot slingshot;
    public ArrayList<SuperObject> inventory;
    public final int maxInventorySize = 8; //subject to change later
    public int bootsCost = 1, meleeCost = 1, armorCost = 1, projectileCost = 1;

    public Player(GameApplication ga, KeyHandler keyH) {
        this.ga = ga;
        this.keyH = keyH;

        // Sets Screen Coords to Center of Screen
        screenX = ga.SCREEN_WIDTH / 2 - ga.TILE_SIZE / 2;
        screenY = ga.SCREEN_HEIGHT / 2 - ga.TILE_SIZE / 2;

        // Sets Hit box to be Smaller Than Sprite
        solidArea = new Rectangle();
        ((Rectangle)solidArea).setX((double) ga.TILE_SIZE / 6 + worldX);
        ((Rectangle)solidArea).setY((double) ga.TILE_SIZE / 3 + worldY);
        solidAreaDefaultX = (int) ((Rectangle)solidArea).getX();
        solidAreaDefaultY = (int) ((Rectangle)solidArea).getY();
        ((Rectangle)solidArea).setWidth((double) (ga.TILE_SIZE * 2) / 3);
        ((Rectangle)solidArea).setHeight((double) (ga.TILE_SIZE * 2) / 3);

        scythe = new Scythe(ga, this);
        slingshot = new Slingshot(ga, this);
        inventory = new ArrayList<>();

        setDefaultValues();
        getPlayerImage();
        setItems();
    }

    private void pickUpObject(int i) {
        if (i != 999) {
            //pickup only
            if (canObtainItem(ga.obj.get(i))) {
                System.out.println(ga.obj.get(i).name + " acquired!");
                ga.playSE(1);
            }
            else {
                System.out.println("Can't carry anymore!");
            }
            // ga.ui.addMessage(text); ->  implement later
            ga.obj.set(i, null);
        }
    }
    private boolean canObtainItem(SuperObject item) {
        boolean canObtain = false;
        //check if stackable
        if (item.stackable) {
            int index = searchItemInInventory(item.name);
            if (index != 999) {
                ++inventory.get(index).amount;
                canObtain = true;
            }
            else { //New Item to check vacancy
                if(inventory.size() != maxInventorySize) {
                    inventory.add(item);
                    canObtain = true;
                }
            }
        }
        else { //not stackable
            if(inventory.size() != maxInventorySize) {
                inventory.add(item);
                canObtain = true;
            }
        }
        return canObtain;
    }
    private void setItems() {
        SuperObject gold = new OBJ_Gold(ga);
        SuperObject wood = new OBJ_Wood(ga);
        SuperObject stone = new OBJ_Stone(ga);
        wood.amount = 1;
        stone.amount = 1;
        gold.amount = 1;
        inventory.add(gold);
        inventory.add(wood);
        inventory.add(stone);
    }
    // Loads Player Sprites
    private void getPlayerImage() {
        setup("farmer_back_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_back_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_back_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_front_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_front_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_front_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_left_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_left_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_left_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_right_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_right_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_right_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_left_up_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_left_up_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_left_up_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_left_down_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_left_down_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_left_down_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_right_up_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_right_up_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_right_up_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);

        setup("farmer_right_down_1", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_right_down_2", "player", ga.TILE_SIZE, ga.TILE_SIZE);
        setup("farmer_right_down_idle", "player", ga.TILE_SIZE, ga.TILE_SIZE);
    }
    // Set Spawn, Speed, Direction
    public void setDefaultValues() {
        worldX = ga.TILE_SIZE * 64;
        worldY = ga.TILE_SIZE * 50;
        speed = 2;
        direction = "down";

        //Player stats
        maxLife = 6;
        life = maxLife;
        iFrameCount = 60;
    }
    // Update Method
    public void update() {
        // Sets Direction Based on Key Press
        int tempWorldX = worldX;
        int tempWorldY = worldY;

        int diagonalSpeed = (int) (speed / Math.sqrt(2)); // Calculate diagonal speed
        if (KeyHandler.upPressed || KeyHandler.downPressed || KeyHandler.leftPressed || KeyHandler.rightPressed) {
            if (KeyHandler.upPressed) {
                if (KeyHandler.leftPressed) {
                    direction = "left_up";
                    worldY -= diagonalSpeed;
                    worldX -= diagonalSpeed;
                } else if (KeyHandler.rightPressed) {
                    direction = "right_up";
                    worldY -= diagonalSpeed;
                    worldX += diagonalSpeed + 1; //adding 1 doesn't really fix the issue of the speed (it's still just slightly off, but it's much better than before, only real fix would be to convert to double but that would cause a bunch of other errors.
                } else {
                    direction = "up";
                    worldY -= speed;
                }
            }   else if (KeyHandler.downPressed) {
                if (KeyHandler.leftPressed) {
                    direction = "left_down";
                    worldY += diagonalSpeed;
                    worldX -= diagonalSpeed;
                } else if (KeyHandler.rightPressed) {
                    direction = "right_down";
                    worldY += diagonalSpeed;
                    worldX += diagonalSpeed + 1;
                } else {
                    direction = "down";
                    worldY += speed;
                }
            } else if (KeyHandler.leftPressed) {
                direction = "left";
                worldX -= speed;
            } else if (KeyHandler.rightPressed) {
                direction = "right";
                worldX += speed;
            }
        }

        // Changes Hitbox Coordinates
        ((Rectangle)solidArea).setX((double) ga.TILE_SIZE / 6 + worldX);
        ((Rectangle)solidArea).setY((double) ga.TILE_SIZE / 3 + worldY);

        // Checks Collision
        collisionOn = false;
        ga.cChecker.checkTile(this);
        ga.cChecker.checkResource(this);
        int objIndex = ga.cChecker.checkObject(this);
        pickUpObject(objIndex);
        List<Integer> monIndices = ga.cChecker.checkMonster(this);
        for (Integer index : monIndices) {
            takeDamage(ga.greenSlime.get(index).attackValue);
        }

/*
        int resourceIndex = ga.cChecker.checkResource(this);*/


        // Stops Player if Collision is On
        if (collisionOn) {
            worldX = tempWorldX;
            worldY = tempWorldY;
        }

        // Walking Animation
        ++spriteCounter;
        if (spriteCounter > 10) {
            if (spriteNum == 1) {
                spriteNum = 2;
            } else {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }

        // Resets Animation Timer and Sets Sprite to Idle
        if (!KeyHandler.upPressed && !KeyHandler.downPressed && !KeyHandler.leftPressed && !KeyHandler.rightPressed) {
            spriteNum = 3;
            spriteCounter = 0;
        }

        iFrameCount++;

        // Weapon
        scythe.update();
        slingshot.update();

    }
    public void takeDamage(int damage) {
        if (iFrameCount >= 60) {
            life -= damage;
            iFrameCount = 0;
            ga.playSE(2);
        }
        if (life <= 0) {
            ga.gameState = ga.END_STATE;
        }
    }
    public int searchItemInInventory(String itemName) {
        int itemIndex = 999;
        for(int i = 0; i < inventory.size(); i++) {
            if (inventory.get(i).name.equals(itemName)) {
                itemIndex = i;
                break;
            }
        }
        return itemIndex;
    }
    public boolean hasRequiredItems(int cost) {
        int goldIndex = searchItemInInventory("Gold");
        int stoneIndex = searchItemInInventory("Stone");
        int woodIndex = searchItemInInventory("Wood");

    return goldIndex != 999 && inventory.get(goldIndex).amount >= cost &&
            stoneIndex != 999 && inventory.get(stoneIndex).amount >= cost &&
            woodIndex != 999 && inventory.get(woodIndex).amount >= cost;
}
    public void upgradeBoots() {
        if (hasRequiredItems(bootsCost)) {
            int goldIndex = searchItemInInventory("Gold");
            int stoneIndex = searchItemInInventory("Stone");
            int woodIndex = searchItemInInventory("Wood");

            inventory.get(goldIndex).amount -= bootsCost;
            inventory.get(stoneIndex).amount -= bootsCost;
            inventory.get(woodIndex).amount -= bootsCost;

            // apply upgrade to player
            speed++;
            // Update the costs for the next upgrade
            ++bootsCost;
            ga.playSE(8);
        }
    }
    public void upgradeArmor() {
        // Check if player has required items in inventory
        if (hasRequiredItems(armorCost)) {
            int goldIndex = searchItemInInventory("Gold");
            int stoneIndex = searchItemInInventory("Stone");
            int woodIndex = searchItemInInventory("Wood");

            inventory.get(goldIndex).amount -= armorCost;
            inventory.get(stoneIndex).amount -= armorCost;
            inventory.get(woodIndex).amount -= armorCost;

            //apply upgrade
            maxLife++;
            life++;

            // Update the costs for the next upgrade
            ++armorCost;
            ga.playSE(8);
        }
    }
    // Render Method
    public void render(GraphicsContext gc) {
        Image image = null;

        // Sets Sprite Based on Direction and Animation Frame
        switch(scythe.direction) {
            case "up" ->
                image = images.get(-1 + spriteNum);

            case "down" ->
                image = images.get(2 + spriteNum);

            case "left" ->
                image = images.get(5 + spriteNum);

            case "right" ->
                image = images.get(8 + spriteNum);

            case "left_up" ->
                image = images.get(11 + spriteNum);

            case "left_down" ->
                image = images.get(14 + spriteNum);

            case "right_up" ->
                image = images.get(17 + spriteNum);

            case "right_down" ->
                image = images.get(20 + spriteNum);


        }

        // Renders Player
        gc.drawImage(image, screenX, screenY);

        // Renders Player Hit box
        //gcPlayer.setStroke(Color.TRANSPARENT);
        if (ga.showExtraHUD) {
            gc.strokeRect(screenX + 8, screenY + 16, 32, 32);
        }
        scythe.render(gc);
        slingshot.render(gc);
    }

}
