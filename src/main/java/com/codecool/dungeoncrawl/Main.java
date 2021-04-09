package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Cell;
import com.codecool.dungeoncrawl.logic.GameMap;
import com.codecool.dungeoncrawl.logic.MapLoader;
import com.codecool.dungeoncrawl.logic.actors.Enemy;
import com.codecool.dungeoncrawl.logic.actors.Player;
import com.codecool.dungeoncrawl.logic.items.*;
import com.codecool.dungeoncrawl.model.GameState;
import com.codecool.dungeoncrawl.model.ItemModel;
import com.codecool.dungeoncrawl.model.PlayerModel;
import javafx.application.Application;
import javafx.application.Platform;
import com.codecool.dungeoncrawl.dao.GameDatabaseManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
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

    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) {
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
        fileLoad.setOnAction(e -> loadGame(currentMap.getPlayer()));

        file.getItems().addAll(fileRestart, fileSave, fileLoad, fileSeparator, fileExit);


        createMenu();

        VBox root = new VBox(menuBar);

        // ui
        changeGridColor();

        ui.setPrefWidth(300);
        ui.setPadding(new Insets(10));

        createPane();
        createLabels();

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

    private void createLabels() {
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
                inventoryLabel.setText("" + inventoryLabel.getText() + "\n" + item.getTileName());
                inventoryLabel.setTextFill(Color.web("#ffffff", 1));
            }

        });
    }

    private void createMenu() {
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
        fileLoad.setOnAction(e -> loadGame());

        file.getItems().addAll(fileRestart, fileSave, fileLoad, fileSeparator, fileExit);
    }

    private void createPane() {
        final Pane spring = new Pane();

        TextField textField = new TextField();
        HBox hbox = new HBox(textField, submitButton);
        ui.add(hbox, 0, 0);
        ui.add(spring, 0, 7);

        submitButton.setOnAction(action -> {
            String inputName = textField.getText();
            name.setText(inputName);
            name.setTextFill(Color.web("#ffffff", 1));
            currentMap.getPlayer().setName(inputName);
            ui.getChildren().remove(hbox);

            Label label = new Label();
            label.setText("Player's name: ");
            label.setTextFill(Color.web("#ffffff", 1));
            ui.add(label, 0, 0);
            ui.add(name, 1, 0);

        });
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
        } else if (currentMapIndex == 1) {
            ui.setStyle("-fx-background-color: #8a6500");
        } else if (currentMapIndex == 2) {
            ui.setStyle("-fx-background-color: #c20000");
        }
    }


    public void changeButtonStateOnItem() {
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
                currentMap.getPlayer().move(1, 0);
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
                if (keyEvent.isControlDown()) {
                    if (!dbManager.isPlayerExistsInDb(currentMap.getPlayer().getName())) {
                        dbManager.save(currentMap.getPlayer(), maps);
                    } else {
                        isSave(currentMap.getPlayer());
                    }
                }
                savedGameModal();
                break;
            case M:
                showMiniMap();
                break;
        }
    }

    private void isSave(Player player) {
        Stage confirmWindow = new Stage();

        confirmWindow.initModality(Modality.APPLICATION_MODAL);
        confirmWindow.setTitle("Confirmation");

        Label label1 = new Label("Would you like to overwrite \n the already existing state or rename and save?");
        Button yesButton = new Button("Overwrite");
        Button noButton = new Button("Rename and save");
        TextField newName = new TextField();

        yesButton.setOnAction(value -> {
            dbManager.update(player, maps, currentMapIndex);
            confirmWindow.close();
        });

        noButton.setOnAction(e -> {
            player.setName(newName.getText());
            name.setText(player.getName());
            dbManager.save(currentMap.getPlayer(), maps);
            confirmWindow.close();
        });

        VBox layout = new VBox(10);

        layout.getChildren().addAll(label1, yesButton, noButton, newName);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 250, 200);

        confirmWindow.setScene(scene1);

        confirmWindow.showAndWait();
    }

    public void savedGameModal() {
        Stage savedGame = new Stage();

        savedGame.initModality(Modality.APPLICATION_MODAL);
        savedGame.setTitle("Saved Game");

        Label saveLabel = new Label();
        saveLabel.setText("Successfully saved the Game!");
        VBox layout = new VBox(10);

        layout.getChildren().addAll(saveLabel);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 499, 400);

        savedGame.setScene(scene1);
        savedGame.show();
    }


    private void showMiniMap() {

        Stage MiniMap = new Stage();

        MiniMap.initModality(Modality.APPLICATION_MODAL);
        MiniMap.setTitle("MINIMAP");

        Label map1 = new Label();

        Image img1 = new Image("/minimap1.png");
        ImageView view1 = new ImageView(img1);
        Image img2 = new Image("/minimap2.png");
        ImageView view2 = new ImageView(img2);
        Image img3 = new Image("/minimap3.png");
        ImageView view3 = new ImageView(img3);

        view1.setPreserveRatio(true);
        view2.setPreserveRatio(true);
        view3.setPreserveRatio(true);

        if (currentMapIndex == 0) {
            map1.setGraphic(view1);
        } else if (currentMapIndex == 1) {
            map1.setGraphic(view2);
        } else if (currentMapIndex == 2) {
            map1.setGraphic(view3);
        }

        VBox layout = new VBox(10);

        layout.getChildren().addAll(map1);
        layout.setAlignment(Pos.CENTER);

        Scene scene1 = new Scene(layout, 499, 400);

        MiniMap.setScene(scene1);
        MiniMap.show();

    }


    private void loadGame(Player player) {

        Stage loadGame = new Stage();

        loadGame.initModality(Modality.APPLICATION_MODAL);
        loadGame.setTitle("LOAD GAME");

        VBox layout = new VBox(10);

        Label load = new Label();
        Label names = new Label();
        for (String name : dbManager.loadSaves()) {
            names.setText(names.getText() + "\n" + name);
        }
        TextField chosenName = new TextField();
        Button confirm = new Button("Choose");

        confirm.setOnAction(e -> {

            maps.clear();
            player.setName(chosenName.getText());
            name.setText(player.getName());
            changePlayerStats(currentMap.getPlayer(), chosenName.getText());
            healthLabel.setText("" + currentMap.getPlayer().getHealth());
            strengthLabel.setText("" + currentMap.getPlayer().getStrength());
            List<Item> fullInventory = currentMap.getPlayer().getInventory();
            inventoryLabel.setText("");
            for (Item item : fullInventory) {
                inventoryLabel.setText("" + inventoryLabel.getText() + "\n" + item.getTileName());
            }
            addMapsOnLoad(chosenName.getText());
            currentMap = maps.get(currentMapIndex);
            loadGame.close();
            refresh();

        });

        layout.getChildren().addAll(load, names, chosenName, confirm);

        layout.setAlignment(Pos.CENTER);
        Scene scene1 = new Scene(layout, 300, 300);

        loadGame.setScene(scene1);
        loadGame.show();
    }


    private void refresh() {
        enemies = currentMap.getEnemiesOnCurrentMap();
        if (currentMap.getPlayer().isOnDoorDown()) {
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
                } else if (cell.getItem() != null) {
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
            inventoryLabel.setText("" + inventoryLabel.getText() + "\n" + item.getTileName());
        }

    }

    private void addMaps() {
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

    public void addMapsOnLoad(String playerName) {
        List<GameState> gameMaps = dbManager.loadGameMaps(playerName);
        for (GameState gameMap : gameMaps) {
            String map1 = gameMap.getMap1();
            String map2 = gameMap.getMap2();
            String map3 = gameMap.getMap3();
            maps.add(MapLoader.loadMap(map1));
            maps.add(MapLoader.loadMap(map2));
            maps.add(MapLoader.loadMap(map3));
            currentMapIndex = gameMap.getCurrentMap();
        }
    }

    public void changePlayerStats(Player player, String newName) {
        PlayerModel playerModel = dbManager.loadPlayer(newName);
        player.setName(playerModel.getPlayerName());
        player.setStrength(playerModel.getStrength());
        player.setHealth(playerModel.getHealth());
        addItemsToLoadedPlayer(player);

    }

    private void addItemsToLoadedPlayer(Player player) {
        player.clearInventory();
        List<ItemModel> itemModels = dbManager.loadInventory(player.getName());
        if (itemModels != null) {
            for (ItemModel itemModel : itemModels) {
                player.addItemToInventoryOnLoad(createItem(itemModel.getItemType(), itemModel.getItemName()));
            }
        }
    }

    public Item createItem(String type, String name) {
        Cell cell = new Cell(null, 1000, 1000, null);
        if (type.equals("Key")) {
            switch (name) {
                case "bronze":
                    return new Key(cell, KeyType.BRONZE_KEY);
                case "silver":
                    return new Key(cell, KeyType.SILVER_KEY);
            }
        } else if (type.equals("Potion")) {
            switch (name) {
                case "strong healing potion":
                    return new Potion(cell, PotionType.STRONG_HEALTH_POTION);
                case "weak healing potion":
                    return new Potion(cell, PotionType.WEAK_HEALTH_POTION);
                case "extra healing potion":
                    return new Potion(cell, PotionType.EXTRA_HEALTH_POTION);
            }
        } else if (type.equals("Weapon")) {
            switch (name) {
                case "crossbow":
                    return new Weapon(cell, WeaponType.CROSSBOW);
                case "sword":
                    return new Weapon(cell, WeaponType.SWORD);
                case "axe":
                    return new Weapon(cell, WeaponType.AXE);
            }
        }
        return null;
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

