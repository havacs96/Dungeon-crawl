package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Enemy;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.Item;
import javafx.application.Application;
import javafx.application.Platform;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import java.sql.SQLException;


import java.util.ArrayList;
import java.util.List;


public class Main extends Application {
    int currentMapIndex;
    ArrayList<GameMap> maps = new ArrayList<>();
    GameMap currentMap;
    Canvas canvas;
    GraphicsContext context;
    Label healthLabel = new Label();
    Label strengthLabel = new Label();
    Label inventoryLabel = new Label();
    Button button = new Button("Pick Up!");
    Button submitButton = new Button("Submit");
    Label name = new Label();


    List<Enemy> enemies;

    MenuBar menuBar = new MenuBar();
    GameDatabaseManager dbManager;

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        setupDbManager();
        addMaps();
        currentMap = maps.get(currentMapIndex);
        canvas = new Canvas(
                currentMap.getWidth() * Tiles.TILE_WIDTH,
                currentMap.getHeight() * Tiles.TILE_WIDTH);
        context = canvas.getGraphicsContext2D();


        Menu file = new Menu("File");
        Menu authors = new Menu("Authors");
        Menu help = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(file, authors, help);

        MenuItem fileRestart = new MenuItem("Restart");
        MenuItem fileSave = new MenuItem("Save");
        MenuItem fileLoad = new MenuItem("Load");
        MenuItem fileSeparator = new SeparatorMenuItem();
        MenuItem fileExit = new MenuItem("Exit");
        fileExit.setOnAction(e -> Platform.exit());

        file.getItems().addAll(fileRestart, fileSave, fileLoad, fileSeparator, fileExit);

        VBox root = new VBox(menuBar);


        // ui
        GridPane ui = new GridPane();

        ui.setPrefWidth(300);
        ui.setPadding(new Insets(10));

        final Pane spring = new Pane();


        TextField textField = new TextField();
        HBox hbox = new HBox(textField, submitButton);
        ui.add(hbox, 0, 0);

        submitButton.setOnAction(action -> {
            String inputName = textField.getText();
            name.setText(inputName);
            currentMap.getPlayer().setName(inputName);
            ui.getChildren().remove(hbox);
            ui.add(new Label("Player's name: "), 0, 0);
            ui.add(name, 1, 0);
        });

        ui.add(new Label("Health: "), 0, 1);
        ui.add(healthLabel, 1, 1);
        ui.add(new Label("Strength: "), 0, 2);
        ui.add(strengthLabel, 1, 2);
        ui.add(new Label("Use weak health \n potion with key: w \n\n"), 0, 3);
        ui.add(new Label("Use strong health \n potion with key: a \n"), 0, 4);
        ui.add(new Label("Use extra health \n potion with key: e \n\n"), 0, 5);


        ui.add(button, 0, 6);
        ui.add(spring, 0, 7);
        ui.add(new Label("Inventory: "), 0, 8);
        ui.add(inventoryLabel, 0, 9);

        button.setDisable(true);
        button.setFocusTraversable(false);
        button.setOnAction((event) -> {

            currentMap.getPlayer().addItemToInventory();
            button.setDisable(true);
            currentMap.getPlayer().removeItem();
            List<Item> fullInventory = currentMap.getPlayer().getInventory();
            inventoryLabel.setText("");
            for (Item item : fullInventory) {
                inventoryLabel.setText("" +inventoryLabel.getText() + "\n" + item.getTileName());
            }

        });

        BorderPane borderPane = new BorderPane();

        borderPane.setCenter(canvas);
        borderPane.setRight(ui);
        borderPane.setTop(root);

        Scene scene = new Scene(borderPane);

        primaryStage.setScene(scene);
        refresh();
        scene.setOnKeyPressed(this::onKeyPressed);
        scene.setOnKeyReleased(this::onKeyReleased);

        primaryStage.setTitle("Dungeon Crawl by Code of the LordCool!");
        primaryStage.show();
    }

    private void onKeyReleased(KeyEvent keyEvent) {
        KeyCombination exitCombinationMac = new KeyCodeCombination(KeyCode.W, KeyCombination.SHORTCUT_DOWN);
        KeyCombination exitCombinationWin = new KeyCodeCombination(KeyCode.F4, KeyCombination.ALT_DOWN);
        if (exitCombinationMac.match(keyEvent)
                || exitCombinationWin.match(keyEvent)
                || keyEvent.getCode() == KeyCode.ESCAPE) {
            exit();
        }
    }


    public void changeButtonStateOnItem(){
        if (currentMap.getPlayer().isOnItem()) {
            button.setDisable(false);
        } else {
            button.setDisable(true);
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
            case W:
                currentMap.getPlayer().usePotion("w");
                refresh();
                break;
            case A:
                currentMap.getPlayer().usePotion("s");
                refresh();
                break;
            case E:
                currentMap.getPlayer().usePotion("e");
                refresh();
                break;
            case S:
                Player player = currentMap.getPlayer();
                dbManager.savePlayer(player);
                break;
        }
    }


    private void refresh() {
        enemies = currentMap.getEnemiesOnCurrentMap();
        if (currentMap.getPlayer().isOnDoorDown()){
            Player currentPlayer = currentMap.getPlayer();
            currentMapIndex++;
            currentMap = maps.get(currentMapIndex);
            Cell currentCell = currentMap.getDoorDown();
            currentMap.setPlayer(currentPlayer);
            currentMap.getPlayer().setCell(currentCell);
        } else if (currentMap.getPlayer().isOnDoorUp()) {
            Player currentPlayer = currentMap.getPlayer();
            currentMapIndex--;
            currentMap = maps.get(currentMapIndex);
            Cell currentCell = currentMap.getDoorUp();
            currentMap.setPlayer(currentPlayer);
            currentMap.getPlayer().setCell(currentCell);
        }
        for (Enemy enemy : enemies) {
            enemy.monsterMove();
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
        strengthLabel.setText("" + currentMap.getPlayer().getStrength());
        List<Item> fullInventory = currentMap.getPlayer().getInventory();
        inventoryLabel.setText("");
        for (Item item : fullInventory) {
            inventoryLabel.setText("" +inventoryLabel.getText() + "\n" + item.getTileName());
        }

    }

    private void addMaps(){
        String[] mapFiles = {"/map.txt", "/map2.txt", "/map3.txt"};
        for (String mapFile : mapFiles) {
            maps.add(MapLoader.loadMap(mapFile));
        }
    }


    private void setupDbManager() {
        dbManager = new GameDatabaseManager();
        try {
            dbManager.setup();
        } catch (SQLException ex) {
            System.out.println("Cannot connect to database.");
        }
    }

    private void exit() {
        try {
            stop();
        } catch (Exception e) {
            System.exit(1);
        }
        System.exit(0);
    }
}

