import java.awt.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

class Customer {
    String name, contact;
    Customer(String name, String contact) { this.name = name; this.contact = contact; }
}

class WasteItem {
    String category, item, material;
    double rate;
    WasteItem(String category, String item, String material, double rate) {
        this.category = category; this.item = item; this.material = material; this.rate = rate;
    }
}

class CollectionRecord {
    String ref, area, method;
    Customer customer;
    WasteItem waste;
    double weight, value;
    CollectionRecord(String ref, Customer c, WasteItem w, double weight,
                     String method, String area, double value) {
        this.ref = ref; this.customer = c; this.waste = w; this.weight = weight;
        this.method = method; this.area = area; this.value = value;
    }
}

class RecyclingData {
    Map<String, String[]> catItems = new LinkedHashMap<>();
    Map<String, String[]> itemMaterials = new LinkedHashMap<>();
    Map<String, Double> itemRate = new LinkedHashMap<>();
    String[] areas = {"Sion", "Dadar", "Matunga", "Kurla", "Chembur", "Andheri", "Bandra", "Powai"};
    String[] methods = {"Material Recovery", "Battery Recycling", "Component Recovery", "Shredding & Reuse", "Smelting"};

    RecyclingData() {
        catItems.put("Computers", new String[]{"Laptop", "Desktop PC", "Monitor", "Keyboard"});
        catItems.put("Mobile Devices", new String[]{"Mobile Phone", "Tablet", "Smart Watch"});
        catItems.put("Batteries", new String[]{"Lithium Battery", "Lead Acid Battery", "Button Cell"});
        catItems.put("Home Electronics", new String[]{"Television", "Printer", "Microwave"});
        catItems.put("Electronic Accessories", new String[]{"Charger", "USB Cable", "Power Adapter", "Earphones"});

        itemMaterials.put("Laptop", new String[]{"Plastic & Metal", "Circuit Board", "Lithium Battery"});
        itemMaterials.put("Desktop PC", new String[]{"Mixed Plastic & Metal", "Circuit Board"});
        itemMaterials.put("Monitor", new String[]{"Glass & Metal", "Plastic"});
        itemMaterials.put("Keyboard", new String[]{"Plastic", "Metal"});
        itemMaterials.put("Mobile Phone", new String[]{"Glass & Metal", "Lithium Battery"});
        itemMaterials.put("Tablet", new String[]{"Glass & Metal", "Lithium Battery"});
        itemMaterials.put("Smart Watch", new String[]{"Metal & Glass", "Lithium Battery"});
        itemMaterials.put("Lithium Battery", new String[]{"Lithium Battery"});
        itemMaterials.put("Lead Acid Battery", new String[]{"Lead"});
        itemMaterials.put("Button Cell", new String[]{"Alkaline Material"});
        itemMaterials.put("Television", new String[]{"Glass & Metal", "Plastic"});
        itemMaterials.put("Printer", new String[]{"Plastic", "Metal"});
        itemMaterials.put("Microwave", new String[]{"Metal", "Plastic"});
        itemMaterials.put("Charger", new String[]{"Plastic", "Copper"});
        itemMaterials.put("USB Cable", new String[]{"Copper", "Plastic"});
        itemMaterials.put("Power Adapter", new String[]{"Plastic", "Copper"});
        itemMaterials.put("Earphones", new String[]{"Plastic", "Copper"});

        itemRate.put("Laptop", 650.0); itemRate.put("Desktop PC", 450.0); itemRate.put("Monitor", 300.0);
        itemRate.put("Keyboard", 80.0); itemRate.put("Mobile Phone", 700.0); itemRate.put("Tablet", 850.0);
        itemRate.put("Smart Watch", 600.0); itemRate.put("Lithium Battery", 150.0); itemRate.put("Lead Acid Battery", 120.0);
        itemRate.put("Button Cell", 40.0); itemRate.put("Television", 250.0); itemRate.put("Printer", 200.0);
        itemRate.put("Microwave", 220.0); itemRate.put("Charger", 100.0); itemRate.put("USB Cable", 60.0);
        itemRate.put("Power Adapter", 70.0); itemRate.put("Earphones", 50.0);
    }

    String[] categories() { return catItems.keySet().toArray(new String[0]); }
    String[] items(String cat) { return catItems.getOrDefault(cat, new String[0]); }
    String[] materials(String item) { return itemMaterials.getOrDefault(item, new String[0]); }
    double rate(String item) { return itemRate.getOrDefault(item, 0.0); }
}

public class Ewaste extends JFrame {
    private static final Color DARK_GREEN = new Color(0x1B5E20);
    private static final Color TEAL = new Color(0x00796B);
    private static final Color LIGHT_TEAL = new Color(0xE0F2F1);
    private static final Color BG = new Color(0xF2F7F4);
    private static final Color BORDER_CLR = new Color(0xB2DFDB);
    private static final Color TEXT_DARK = new Color(0x263238);
    private static final Color TEXT_MUTED = new Color(0x607D8B);
    private static final Color NEUTRAL_BTN = new Color(0x78909C);
    private static final Color DANGER = new Color(0xC62828);

    private final RecyclingData data = new RecyclingData();
    private final List<CollectionRecord> records = new ArrayList<>();
    private int counter = 0;

    private JTextField nameField, contactField, weightField, valueField;
    private JComboBox<String> categoryCombo, itemCombo, materialCombo, areaCombo, methodCombo;
    private JTextArea receiptArea;
    private JLabel totalCollLabel, totalItemsLabel, totalWeightLabel;

    public Ewaste() {
        setTitle("E-Waste Collection & Recycling Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getContentPane().setBackground(BG);
        setLayout(new BorderLayout());

        add(buildHeader(), BorderLayout.NORTH);
        add(buildMainContent(), BorderLayout.CENTER);
        add(buildSummaryBar(), BorderLayout.SOUTH);

        updateItems();
        updateSummary();

        setSize(1600, 900);
        setMinimumSize(new Dimension(1000, 650));
        setLocationRelativeTo(null);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setVisible(true);
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(DARK_GREEN);
        header.setBorder(new EmptyBorder(16, 30, 16, 30));

        JLabel title = new JLabel("E-WASTE COLLECTION & RECYCLING SYSTEM");
        title.setFont(new Font("SansSerif", Font.BOLD, 22));
        title.setForeground(Color.WHITE);

        JLabel subtitle = new JLabel("Smart collection. Responsible recycling.");
        subtitle.setFont(new Font("SansSerif", Font.PLAIN, 12));
        subtitle.setForeground(new Color(0xC8E6C9));

        JPanel textStack = new JPanel();
        textStack.setOpaque(false);
        textStack.setLayout(new BoxLayout(textStack, BoxLayout.Y_AXIS));
        textStack.add(title);
        textStack.add(subtitle);
        header.add(textStack, BorderLayout.WEST);

        JLabel tag = new JLabel("COLLECTION CENTER");
        tag.setFont(new Font("SansSerif", Font.BOLD, 12));
        tag.setForeground(new Color(0xC8E6C9));
        header.add(tag, BorderLayout.EAST);
        return header;
    }

    private JPanel buildMainContent() {
        JPanel columns = new JPanel(new GridLayout(1, 3, 20, 0));
        columns.setBackground(BG);
        columns.setBorder(new EmptyBorder(20, 28, 20, 28));
        columns.add(buildColumn1());
        columns.add(buildColumn2());
        columns.add(buildColumn3());
        return columns;
    }

    private JPanel buildColumn1() {
        JPanel col = new JPanel();
        col.setOpaque(false);
        col.setLayout(new BoxLayout(col, BoxLayout.Y_AXIS));

        nameField = textField();
        contactField = textField();
        JPanel customerCard = compactCard("Customer Information",
                row("Customer Name", nameField), row("Contact Number", contactField));

        categoryCombo = combo(data.categories());
        itemCombo = combo(new String[0]);
        materialCombo = combo(new String[0]);
        categoryCombo.addActionListener(e -> updateItems());
        itemCombo.addActionListener(e -> updateMaterials());
        JPanel classCard = card("E-Waste Classification", false,
                row("E-Waste Category", categoryCombo), row("E-Waste Item", itemCombo), row("Material Type", materialCombo));

        col.add(customerCard);
        col.add(Box.createVerticalStrut(20));
        col.add(classCard);
        col.add(Box.createVerticalGlue());
        return col;
    }

    private JPanel buildColumn2() {
        JPanel col = new JPanel(new BorderLayout(0, 20));
        col.setOpaque(false);

        weightField = textField();
        areaCombo = combo(data.areas);
        methodCombo = combo(data.methods);
        valueField = textField();
        valueField.setEditable(false);
        valueField.setBackground(LIGHT_TEAL);
        valueField.setFont(new Font("SansSerif", Font.BOLD, 13));
        valueField.setForeground(DARK_GREEN);

        JPanel detailsCard = card("Collection Details", true,
                row("Weight (kg)", weightField), row("Collection Area", areaCombo),
                row("Recycling Method", methodCombo), row("Estimated Value (Rs.)", valueField));

        RButton calcBtn = new RButton("Calculate Value", TEAL);
        RButton submitBtn = new RButton("Submit Collection", DARK_GREEN);
        calcBtn.addActionListener(e -> calculateValue());
        submitBtn.addActionListener(e -> submitCollection());

        col.add(detailsCard, BorderLayout.CENTER);
        col.add(buttonBarCard(calcBtn, submitBtn), BorderLayout.SOUTH);
        return col;
    }

    private JPanel buildColumn3() {
        JPanel col = new JPanel(new BorderLayout(0, 20));
        col.setOpaque(false);

        receiptArea = new JTextArea(18, 30);
        receiptArea.setEditable(false);
        receiptArea.setLineWrap(false);
        receiptArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        receiptArea.setForeground(DARK_GREEN);
        receiptArea.setBackground(new Color(0xF4FBF8));
        receiptArea.setBorder(new EmptyBorder(14, 16, 14, 16));
        receiptArea.setText("Your collection confirmation will appear here\nafter you press \"Submit Collection\".");

        JPanel receiptBox = new JPanel(new BorderLayout());
        receiptBox.setBackground(new Color(0xF4FBF8));
        receiptBox.setBorder(BorderFactory.createLineBorder(BORDER_CLR));
        receiptBox.add(receiptArea, BorderLayout.CENTER);

        JPanel confirmCard = new JPanel(new BorderLayout(0, 10));
        confirmCard.setBackground(Color.WHITE);
        confirmCard.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(14, 16, 14, 16)));
        confirmCard.add(heading("Collection Confirmation"), BorderLayout.NORTH);
        confirmCard.add(receiptBox, BorderLayout.CENTER);

        RButton clearBtn = new RButton("Clear", NEUTRAL_BTN);
        RButton exitBtn = new RButton("Exit", DANGER);
        RButton historyBtn = new RButton("View History", NEUTRAL_BTN);
        clearBtn.addActionListener(e -> resetForm());
        exitBtn.addActionListener(e -> System.exit(0));
        historyBtn.addActionListener(e -> viewHistory());

        col.add(confirmCard, BorderLayout.CENTER);
        col.add(buttonBarCard(clearBtn, exitBtn, historyBtn), BorderLayout.SOUTH);
        return col;
    }

    private JPanel buildSummaryBar() {
        JPanel bar = new JPanel(new BorderLayout(0, 4));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, BORDER_CLR), new EmptyBorder(8, 28, 8, 28)));
        bar.add(heading("Collection Summary"), BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 3, 20, 0));
        stats.setOpaque(false);
        totalCollLabel = statValue("0");
        totalItemsLabel = statValue("0");
        totalWeightLabel = statValue("0.00 kg");
        stats.add(statTile("Total Collections", totalCollLabel));
        stats.add(statTile("Total Items Collected", totalItemsLabel));
        stats.add(statTile("Total Weight Collected", totalWeightLabel));
        bar.add(stats, BorderLayout.CENTER);
        return bar;
    }

    private JPanel statTile(String caption, JLabel value) {
        JPanel tile = new JPanel();
        tile.setOpaque(false);
        tile.setLayout(new BoxLayout(tile, BoxLayout.Y_AXIS));
        JLabel c = new JLabel(caption);
        c.setFont(new Font("SansSerif", Font.PLAIN, 12));
        c.setForeground(TEXT_MUTED);
        tile.add(c);
        tile.add(value);
        return tile;
    }

    private JLabel statValue(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 16));
        l.setForeground(TEAL);
        return l;
    }

    private JPanel card(String title, boolean stretch, JPanel... rows) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(18, 20, 18, 20)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel h = heading(title);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(h);
        card.add(Box.createVerticalStrut(10));
        for (JPanel r : rows) {
            r.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(r);
            card.add(Box.createVerticalStrut(10));
        }
        if (stretch) card.add(Box.createVerticalGlue());
        return card;
    }

    private JPanel compactCard(String title, JPanel... rows) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(12, 20, 10, 20)));
        card.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel h = heading(title);
        h.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(h);
        card.add(Box.createVerticalStrut(6));
        for (JPanel r : rows) {
            r.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(r);
            card.add(Box.createVerticalStrut(6));
        }
        return card;
    }

    private JPanel buttonBarCard(RButton... buttons) {
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        bar.setBackground(Color.WHITE);
        bar.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR), new EmptyBorder(14, 16, 14, 16)));
        for (RButton b : buttons) bar.add(b);
        return bar;
    }

    private JLabel heading(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("SansSerif", Font.BOLD, 14));
        l.setForeground(TEAL);
        return l;
    }

    private JPanel row(String labelText, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);
        JLabel l = new JLabel(labelText);
        l.setFont(new Font("SansSerif", Font.BOLD, 12));
        l.setForeground(TEXT_DARK);
        l.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(Integer.MAX_VALUE, 38));
        field.setPreferredSize(new Dimension(150, 38));
        p.add(l);
        p.add(Box.createVerticalStrut(5));
        p.add(field);
        return p;
    }

    private JTextField textField() {
        JTextField f = new JTextField();
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER_CLR, 1), new EmptyBorder(6, 10, 6, 10)));
        return f;
    }

    private JComboBox<String> combo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setFont(new Font("SansSerif", Font.PLAIN, 13));
        c.setBackground(Color.WHITE);
        c.setBorder(BorderFactory.createLineBorder(BORDER_CLR, 1));
        c.setFocusable(false);
        return c;
    }

    private static class RButton extends JButton {
        private final Color bg;
        RButton(String text, Color bg) {
            super(text);
            this.bg = bg;
            setFont(new Font("SansSerif", Font.BOLD, 13));
            setForeground(Color.WHITE);
            setOpaque(false);
            setContentAreaFilled(false);
            setBorderPainted(false);
            setFocusPainted(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(12, 20, 12, 20));
        }
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getModel().isPressed() ? bg.darker() : bg);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
            g2.dispose();
            super.paintComponent(g);
        }
    }

    private void updateItems() {
        String cat = (String) categoryCombo.getSelectedItem();
        itemCombo.removeAllItems();
        if (cat != null) for (String it : data.items(cat)) itemCombo.addItem(it);
        updateMaterials();
    }

    private void updateMaterials() {
        String item = (String) itemCombo.getSelectedItem();
        materialCombo.removeAllItems();
        if (item != null) for (String m : data.materials(item)) materialCombo.addItem(m);
    }

    private double calculateValue() {
        String item = (String) itemCombo.getSelectedItem();
        String w = weightField.getText().trim();
        if (item == null || w.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select an E-Waste Item and enter weight.", "Cannot Calculate", JOptionPane.WARNING_MESSAGE);
            return -1;
        }
        double weight;
        try {
            weight = Double.parseDouble(w);
            if (weight <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid weight.", "Invalid Weight", JOptionPane.ERROR_MESSAGE);
            return -1;
        }
        double value = weight * data.rate(item);
        valueField.setText(String.format("%.2f", value));
        return value;
    }

    private void submitCollection() {
        if (nameField.getText().trim().isEmpty()) { warn("Enter customer name."); return; }
        String contact = contactField.getText().trim();
        if (!contact.matches("\\d{7,15}")) { warn("Enter a valid contact number (7-15 digits)."); return; }
        double value = calculateValue();
        if (value < 0) return;

        counter++;
        String ref = String.format("ENCR-%06d", 100000 + counter);
        Customer customer = new Customer(nameField.getText().trim(), contact);
        String category = (String) categoryCombo.getSelectedItem();
        String item = (String) itemCombo.getSelectedItem();
        String material = (String) materialCombo.getSelectedItem();
        WasteItem waste = new WasteItem(category, item, material, data.rate(item));
        double weight = Double.parseDouble(weightField.getText().trim());
        String area = (String) areaCombo.getSelectedItem();
        String method = (String) methodCombo.getSelectedItem();

        CollectionRecord rec = new CollectionRecord(ref, customer, waste, weight, method, area, value);
        records.add(rec);

        receiptArea.setText(
                "====================================\n" +
                "    E-WASTE COLLECTION RECEIPT\n" +
                "====================================\n" +
                String.format("Customer : %s%n", customer.name) +
                String.format("Contact  : %s%n", customer.contact) +
                String.format("Ref. No. : %s%n", ref) + "\n" +
                String.format("Category : %s%n", category) +
                String.format("Item     : %s%n", item) +
                String.format("Material : %s%n", material) +
                String.format("Weight   : %.2f kg%n", weight) +
                String.format("Value    : Rs %.2f%n", value) + "\n" +
                String.format("Area     : %s%n", area) +
                String.format("Method   : %s%n", method) +
                "------------------------------------\n" +
                "  Thank you for recycling responsibly!\n" +
                "====================================");

        updateSummary();
        JOptionPane.showMessageDialog(this, "Collection " + ref + " registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        clearFields();
    }

    private void viewHistory() {
        if (records.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No collections recorded yet this session.", "Collection History", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] cols = {"Ref. No.", "Customer", "Category", "Item", "Weight (kg)", "Area", "Value (Rs)"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        double totalWeight = 0, totalValue = 0;
        for (CollectionRecord r : records) {
            model.addRow(new Object[]{r.ref, r.customer.name, r.waste.category, r.waste.item,
                    String.format("%.2f", r.weight), r.area, String.format("%.2f", r.value)});
            totalWeight += r.weight;
            totalValue += r.value;
        }
        JTable table = new JTable(model);
        table.setRowHeight(24);
        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(680, 240));

        JLabel summary = new JLabel(String.format("Collections: %d | Total weight: %.2f kg | Total value: Rs %.2f",
                records.size(), totalWeight, totalValue));
        summary.setFont(new Font("SansSerif", Font.BOLD, 12));
        summary.setBorder(new EmptyBorder(10, 4, 0, 4));

        JPanel content = new JPanel(new BorderLayout());
        content.setBorder(new EmptyBorder(10, 10, 10, 10));
        content.add(sp, BorderLayout.CENTER);
        content.add(summary, BorderLayout.SOUTH);

        JOptionPane.showMessageDialog(this, content, "Collection History - This Session", JOptionPane.PLAIN_MESSAGE);
    }

    private void updateSummary() {
        int n = records.size();
        double totalWeight = 0;
        for (CollectionRecord r : records) totalWeight += r.weight;
        totalCollLabel.setText(String.valueOf(n));
        totalItemsLabel.setText(String.valueOf(n));
        totalWeightLabel.setText(String.format("%.2f kg", totalWeight));
    }

    private void clearFields() {
        nameField.setText("");
        contactField.setText("");
        weightField.setText("");
        valueField.setText("");
        if (categoryCombo.getItemCount() > 0) categoryCombo.setSelectedIndex(0);
        updateItems();
        if (areaCombo.getItemCount() > 0) areaCombo.setSelectedIndex(0);
        if (methodCombo.getItemCount() > 0) methodCombo.setSelectedIndex(0);
    }

    private void resetForm() {
        clearFields();
        receiptArea.setText("Your collection confirmation will appear here\nafter you press \"Submit Collection\".");
    }

    private void warn(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Ewaste::new);
    }
}