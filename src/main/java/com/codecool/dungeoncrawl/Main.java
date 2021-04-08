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
    GridPane ui = new GridPane();


    List<Enemy> enemies;

    MenuBar menuBar = new MenuBar();
    GameDatabaseManager dbManager;

    public void main(String[] args) {
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
        changeGridColor();


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

            Label label = new Label();
            label.setText("Player's name: ");
            label.setTextFill(Color.web("#ffffff", 1));
            ui.add(label, 0, 0);
            ui.add(name, 1, 0);

        });

        Label healthLabelText = new Label();
        healthLabelText.setText("Health: ");
        healthLabelText.setTextFill(Color.web("#ffffff", 1));
        ui.add(healthLabelText, 0, 1);
        healthLabel.setTextFill(Color.web("#ffffff", 1));
        ui.add(healthLabel, 1, 1);

        Label strLabelText = new Label();
        strLabelText.setText("Strength: ");
        strLabelText.setTextFill(Color.web("#ffffff", 1));
        ui.add(strLabelText, 0, 2);
        strengthLabel.setTextFill(Color.web("#ffffff", 1));
        ui.add(strengthLabel, 1, 2);

        Label label1 = new Label();
        label1.setText("Use weak health \n potion with key: w \n\n");
        label1.setTextFill(Color.web("#ffffff", 1));
        ui.add(label1, 0, 3);

        Label label2 = new Label();
        label2.setText("Use strong health \n potion with key: a \n");
        label2.setTextFill(Color.web("#ffffff", 1));
        ui.add(label2, 0, 4);

        Label label3 = new Label();
        label3.setText("Use extra health \n potion with key: e \n\n");
        label3.setTextFill(Color.web("#ffffff", 1));
        ui.add(label3, 0, 5);

        ui.add(button, 0, 6);
        ui.add(spring, 0, 7);

        Label label4 = new Label();
        label4.setText("Inventory: ");
        label4.setTextFill(Color.web("#ffffff", 1));
        ui.add(label4, 0, 8);
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

    public void changeGridColor() {
        if (currentMapIndex == 0) {
            ui.setStyle("-fx-background-color: #000000");
        }
        else if (currentMapIndex == 1) {
            ui.setStyle("-fx-background-color: #8a6500");
        }
        else if (currentMapIndex == 2) {
            ui.setStyle("-fx-background-color: #c20000");
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
                dbManager.save(player, maps);
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
            changeGridColor();
        } else if (currentMap.getPlayer().isOnDoorUp()) {
            Player currentPlayer = currentMap.getPlayer();
            currentMapIndex--;
            currentMap = maps.get(currentMapIndex);
            Cell currentCell = currentMap.getDoorUp();
            currentMap.setPlayer(currentPlayer);
            currentMap.getPlayer().setCell(currentCell);
            changeGridColor();
        }
        for (Enemy enemy : enemies) {
            enemy.monsterMove();
        }
        context.setFill(Color.BLACK);
        context.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
        for (int x = 0; x < currentMap.getWidth(); x++) {
            for (int y = 0; y < currentMap.getHeight(); y++) {
                int centeredX = x - currentMap.getPlayer().getX() + 11;
                int centeredY = y - currentMap.getPlayer().getY() + 11;
                Cell cell = currentMap.getCell(x, y);
                if (cell.getActor() != null) {
                    Tiles.drawTile(context, cell.getActor(), centeredX, centeredY);
                }else if (cell.getItem() != null) {
                    Tiles.drawTile(context, cell.getItem(), centeredX, centeredY);
                } else {
                    Tiles.drawTile(context, cell, centeredX, centeredY);
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
        String[] mapFiles = {"/map1.txt", "/map2.txt", "/map3.txt"};
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

