package main;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Unmarshaller;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;


import java.util.concurrent.atomic.AtomicReference;


public class MainClass {

    private static final AtomicReference<List<Room>> rooms = new AtomicReference<>(new ArrayList<>());

    public static void main(String[] args) {

        JLabel actualRoomDescription = new JLabel("Mazmorra");
        actualRoomDescription.setFont(new Font("Arial", Font.BOLD, 20));
        actualRoomDescription.setHorizontalAlignment(SwingConstants.CENTER);
        actualRoomDescription.setVerticalAlignment(SwingConstants.CENTER);

        JButton up = new JButton("Up");
        JButton down = new JButton("Down");
        JButton left = new JButton("Left");
        JButton right = new JButton("Right");

        JPanel mainPanel = new JPanel(new BorderLayout());

        JLabel movementLogger = new JLabel("<html><body>Log");
        movementLogger.setFont(new Font("Arial", Font.BOLD, 20));

        JPanel navigationPanel = new JPanel(new BorderLayout());
        navigationPanel.add(up , BorderLayout.NORTH);
        navigationPanel.add(down, BorderLayout.SOUTH);
        navigationPanel.add(left, BorderLayout.WEST);
        navigationPanel.add(right, BorderLayout.EAST);
        navigationPanel.add(actualRoomDescription, BorderLayout.CENTER);

        JFrame frame = new JFrame();
        frame.add(mainPanel);
        frame.setVisible(true);
        frame.setSize(800, 600);
        frame.setTitle("Mazmorras");
        frame.setResizable(false);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Opciones");
        JMenuItem menuItemSalir = new JMenuItem("Salir");
        JMenuItem menuItemLoad = new JMenuItem("Cargar mapa de mazmorras");
        menu.add(menuItemSalir);
        menu.add(menuItemLoad);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        JTree tree = new JTree();
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        tree.setModel(new DefaultTreeModel(new DefaultMutableTreeNode("Mazmorra")));
        mainPanel.add(tree, BorderLayout.WEST);

        JSplitPane splitPaneVertical = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPaneVertical.setTopComponent(navigationPanel);
        splitPaneVertical.setBottomComponent(movementLogger);

        JSplitPane splitPaneHorizontal =  new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPaneHorizontal.setLeftComponent(tree);
        splitPaneHorizontal.setRightComponent(splitPaneVertical);

        mainPanel.add(splitPaneHorizontal, BorderLayout.CENTER);

        menuItemSalir.addActionListener(e -> System.exit(0));
        menuItemLoad.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
                @Override
                public boolean accept(File f) {
                    return f.getName().toLowerCase().endsWith(".xml") || f.isDirectory();
                }

                @Override
                public String getDescription() {
                    return "XML files";
                }
            });
            fileChooser.showOpenDialog(frame);
            File file = fileChooser.getSelectedFile();
            try {
                JAXBContext context = JAXBContext.newInstance(Mazmorra.class);
                Unmarshaller unmarshaller = context.createUnmarshaller();
                Mazmorra mazmorra = (Mazmorra) unmarshaller.unmarshal(file);
                rooms.set(mazmorra.getRooms());
                actualRoomDescription.setText(mazmorra.getRooms().get(0).getDescription());
                loadTree(tree, mazmorra);
                loadRoom(mazmorra.getRooms().get(0), actualRoomDescription, movementLogger, up, down, left, right);
            } catch (JAXBException exception) {
                exception.printStackTrace();
            }
        });
    }

    private static void loadTree(JTree tree, Mazmorra mazmorra) {
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Mazmorra");
        tree.setModel(new DefaultTreeModel(root));
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        tree.setCellRenderer(renderer);
        for (Room room : mazmorra.getRooms()) {
            DefaultMutableTreeNode roomNode = new DefaultMutableTreeNode(room.getId());
            root.add(roomNode);
            for (Door door : room.getDoors()) {
                DefaultMutableTreeNode doorNode = new DefaultMutableTreeNode(door.getName() + " --> " + door.getDestination());
                roomNode.add(doorNode);
            }
        }
        for (int i = 0; i < tree.getRowCount(); i++) {
            tree.expandRow(i);
        }
    }

    private static void loadRoom(Room room, JLabel actualRoomDescription, JLabel movementLogger, JButton up, JButton down, JButton left, JButton right) {
        up.setEnabled(false);
        down.setEnabled(false);
        left.setEnabled(false);
        right.setEnabled(false);

        actualRoomDescription.setText(room.getDescription());
        movementLogger.setText(movementLogger.getText().replace("</body></html>","") + "<br> Has ido a " + room.getId() +"</body></html>");

        room.getDoors().forEach(door -> {
            if (door.getName().equalsIgnoreCase("Norte")) {
                up.setEnabled(true);
                up.removeActionListener(up.getActionListeners().length > 0 ? up.getActionListeners()[0] : null);
                up.addActionListener(e -> {
                    Room nextRoom = rooms.get().stream()
                            .filter(r -> r.getId()
                                    .equalsIgnoreCase(room.getDoors().stream().filter(d -> d.getName().equalsIgnoreCase("Norte"))
                                            .findFirst().get()
                                            .getDestination())
                            ).findFirst().get();

                    loadRoom(nextRoom, actualRoomDescription, movementLogger, up, down, left, right);
                });
            } else if (door.getName().equalsIgnoreCase("Sur")) {
                down.setEnabled(true);
                down.removeActionListener(down.getActionListeners().length > 0 ? down.getActionListeners()[0] : null);
                down.addActionListener(e -> {
                    Room nextRoom = rooms.get().stream()
                            .filter(r -> r.getId()
                                    .equalsIgnoreCase(room.getDoors().stream().filter(d -> d.getName().equalsIgnoreCase("Sur"))
                                            .findFirst().get()
                                            .getDestination())
                            ).findFirst().get();

                    loadRoom(nextRoom, actualRoomDescription, movementLogger, up, down, left, right);
                });
            } else if (door.getName().equalsIgnoreCase("Oeste")) {
                left.setEnabled(true);
                left.removeActionListener(left.getActionListeners().length > 0 ? left.getActionListeners()[0] : null);
                left.addActionListener(e -> {
                    Room nextRoom = rooms.get().stream()
                            .filter(r -> r.getId()
                                    .equalsIgnoreCase(room.getDoors().stream().filter(d -> d.getName().equalsIgnoreCase("Oeste"))
                                            .findFirst().get()
                                            .getDestination())
                            ).findFirst().get();

                    loadRoom(nextRoom, actualRoomDescription, movementLogger, up, down, left, right);
                });
            } else if (door.getName().equalsIgnoreCase("Este")) {
                right.setEnabled(true);
                right.removeActionListener(right.getActionListeners().length > 0 ? right.getActionListeners()[0] : null);
                right.addActionListener(e -> {
                    Room nextRoom = rooms.get().stream()
                            .filter(r -> r.getId()
                                    .equalsIgnoreCase(room.getDoors().stream().filter(d -> d.getName().equalsIgnoreCase("Este"))
                                            .findFirst().get()
                                            .getDestination())
                            ).findFirst().get();

                    loadRoom(nextRoom, actualRoomDescription, movementLogger, up, down, left, right);
                });
            }
        });
    }
}