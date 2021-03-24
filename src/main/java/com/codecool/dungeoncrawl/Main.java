package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.EnemyType;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import com.codecool.dungeoncrawl.logic.items.Key;
import com.codecool.dungeoncrawl.logic.items.KeyType;
import com.codecool.dungeoncrawl.logic.items.PotionType;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class Main extends Application {
    int currentMapIndex;
    ArrayList<GameMap> maps = new ArrayList<>();
    GameMap currentMap;
    Canvas canvas;
    GraphicsContext context;
    Label healthLabel = new Label();
    Label inventoryLabel = new Label();
    Button button = new Button("Pick Up!");

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        addMaps();
        currentMap = maps.get(currentMapIndex);
        canvas = new Canvas(
                currentMap.getWidth() * Tiles.TILE_WIDTH,
                currentMap.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();

        GridPane ui = new GridPane();


        ui.setPrefWidth(200);
        ui.setPadding(new Insets(10));

        ui.add(new Label("Health: "), 0, 0);
        ui.add(healthLabel, 1, 0);
        ui.add(new Label("Inventory: "), 0, 3);
        ui.add(inventoryLabel, 0, 4);
        ui.add(button, 0, 2);

        button.setDisable(true);

        button.setOnAction((event) -> {
            currentMap.getPlayer().addItemToInventory();
            button.setDisable(true);
            List<Item> fullInventory = currentMap.getPlayer().getInventory();
            inventoryLabel.setText("");
            for (Item item : fullInventory) {
                inventoryLabel.setText("" +inventoryLabel.getText() + "\n" + item.getTileName());
            }

        });

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);

        Scene scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);

        primaryStage.setTitle("Dungeon Crawl by Code of the LordCool!");
        primaryStage.show();
    }


    public void changeButtonStateOnItem(){
        if (currentMap.getPlayer().isOnItem()){
            button.setDisable(false);
        }
    }

    private void onKeyPressed(KeyEvent keyEvent) {
        switch (keyEvent.getCode()) {
            case UP:
                currentMap.getPlayer().move(0, -1);
                changeButtonStateOnItem();
                refresh();
                break;
            case DOWN:
                currentMap.getPlayer().move(0, 1);
                changeButtonStateOnItem();
                refresh();
                break;
            case LEFT:
                currentMap.getPlayer().move(-1, 0);
                changeButtonStateOnItem();
                refresh();
                break;
            case RIGHT:
                currentMap.getPlayer().move(1,0);
                changeButtonStateOnItem();
                refresh();
                break;
        }
    }


    private void refresh() {
        if (currentMap.getPlayer().isOnDoor()){
            Player currentPlayer = currentMap.getPlayer();
            currentMapIndex++;
            currentMap = maps.get(currentMapIndex);
            Cell currentCell = currentMap.getDoor();
            currentMap.setPlayer(currentPlayer);
            currentMap.getPlayer().setCell(currentCell);
        }
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < currentMap.getWidth(); x++) {
            for (int y = 0; y < currentMap.getHeight(); y++) {
                Cell cell = currentMap.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), x, y);
                }else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), x, y);
                } else {
                    Tiles.drawTile(context, cell, x, y);
                }
            }
        }
        healthLabel.setText("" + currentMap.getPlayer().getHealth());

    }

    private void addMaps(){
        String[] mapFiles = {"/map.txt", "/map2.txt", "/map3.txt"};
        for (String mapFile : mapFiles) {
            maps.add(MapLoader.loadMap(mapFile));
        }
    }

}

