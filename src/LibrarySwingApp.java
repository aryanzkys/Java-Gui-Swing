import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableModel;

public class LibrarySwingApp {
    private static final String STUDENT_FILE = "data/siswa.txt";
    private static final String BOOK_FILE = "data/buku.txt";
    private static final String EMPLOYEE_FILE = "data/pegawai.txt";
    private static final String TRANSACTION_FILE = "data/transaksi.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private JFrame frame;

    private DefaultTableModel studentTableModel;
    private JTable studentTable;
    private JTextField studentNisField;
    private JTextField studentNameField;
    private JTextField studentAddressField;
    private JTextField studentSearchField;

    private DefaultTableModel bookTableModel;
    private JTable bookTable;
    private JTextField bookCodeField;
    private JTextField bookTitleField;
    private JTextField bookTypeField;
    private JTextField bookSearchField;

    private DefaultTableModel employeeTableModel;
    private JTable employeeTable;
    private JTextField employeeNipField;
    private JTextField employeeNameField;
    private JTextField employeeBirthDateField;

    private DefaultTableModel transactionTableModel;
    private JTable transactionTable;
    private JTextField trxCodeField;
    private JTextField trxNisField;
    private JTextField trxBookCodeField;
    private JTextField trxBorrowDateField;
    private JTextField trxReturnDateField;
    private JTextField trxReturnCodeField;

    private DefaultTableModel reportTableModel;
    private JTable reportTable;
    private JLabel reportSummaryLabel;

    public void start() {
        initializeDataFiles();
        SwingUtilities.invokeLater(() -> {
            setupLookAndFeel();
            frame = new JFrame("Junior High School Library Management System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(980, 640);
            frame.setLocationRelativeTo(null);
            frame.setLayout(new BorderLayout());

            if (!showLoginDialog()) {
                frame.dispose();
                return;
            }

            frame.add(buildHeader(), BorderLayout.NORTH);
            frame.add(buildTabs(), BorderLayout.CENTER);

            frame.setVisible(true);
        });
    }

    private void setupLookAndFeel() {
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ignored) {
            // fallback ke default look and feel
        }
    }

    private boolean showLoginDialog() {
        JTextField nipField = new JTextField(18);
        JTextField nameField = new JTextField(18);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("NIP:"), gbc);
        gbc.gridx = 1;
        panel.add(nipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nama:"), gbc);
        gbc.gridx = 1;
        panel.add(nameField, gbc);

        while (true) {
            int result = JOptionPane.showConfirmDialog(frame, panel, "Login Pegawai", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if (result != JOptionPane.OK_OPTION) {
                return false;
            }

            String nip = nipField.getText().trim();
            String name = nameField.getText().trim();

            if (nip.isEmpty() || name.isEmpty()) {
                showMessage("NIP dan Nama tidak boleh kosong.");
                continue;
            }

            Employee employee = findEmployeeByCredentials(nip, name);
            if (employee != null) {
                showMessage("Login berhasil. Selamat datang, " + employee.getName() + "!");
                return true;
            }

            showMessage("Login gagal. NIP atau Nama tidak cocok.");
        }
    }

    private JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        JLabel title = new JLabel("Junior High School Library Management System", SwingConstants.CENTER);
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        header.add(title, BorderLayout.CENTER);
        return header;
    }

    private JTabbedPane buildTabs() {
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Siswa", buildStudentPanel());
        tabs.addTab("Buku", buildBookPanel());
        tabs.addTab("Pegawai", buildEmployeePanel());
        tabs.addTab("Transaksi", buildTransactionPanel());
        tabs.addTab("Laporan", buildReportPanel());
        return tabs;
    }

    private JPanel buildStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        studentTableModel = new DefaultTableModel(new String[]{"NIS", "Nama", "Alamat"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        studentTable = new JTable(studentTableModel);
        studentTable.setPreferredScrollableViewportSize(new Dimension(700, 240));
        studentTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = studentTable.getSelectedRow();
                if (row >= 0) {
                    studentNisField.setText(studentTableModel.getValueAt(row, 0).toString());
                    studentNameField.setText(studentTableModel.getValueAt(row, 1).toString());
                    studentAddressField.setText(studentTableModel.getValueAt(row, 2).toString());
                }
            }
        });
        panel.add(new JScrollPane(studentTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Kelola Siswa"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        studentNisField = new JTextField(16);
        studentNameField = new JTextField(20);
        studentAddressField = new JTextField(22);
        studentSearchField = new JTextField(16);

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("NIS"), gbc);
        gbc.gridx = 1;
        form.add(studentNisField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Nama"), gbc);
        gbc.gridx = 1;
        form.add(studentNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Alamat"), gbc);
        gbc.gridx = 1;
        form.add(studentAddressField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Tambah");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Hapus");
        JButton clearBtn = new JButton("Bersihkan");
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Cari");
        JButton resetBtn = new JButton("Reset");
        searchPanel.add(new JLabel("Kata kunci:"));
        searchPanel.add(studentSearchField);
        searchPanel.add(searchBtn);
        searchPanel.add(resetBtn);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        form.add(searchPanel, gbc);

        panel.add(form, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> handleAddStudent());
        updateBtn.addActionListener(e -> handleUpdateStudent());
        deleteBtn.addActionListener(e -> handleDeleteStudent());
        clearBtn.addActionListener(e -> clearStudentForm());
        searchBtn.addActionListener(e -> handleSearchStudent());
        resetBtn.addActionListener(e -> refreshStudentTable(loadStudents()));

        refreshStudentTable(loadStudents());
        return panel;
    }

    private JPanel buildBookPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        bookTableModel = new DefaultTableModel(new String[]{"Kode", "Judul", "Jenis"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        bookTable = new JTable(bookTableModel);
        bookTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = bookTable.getSelectedRow();
                if (row >= 0) {
                    bookCodeField.setText(bookTableModel.getValueAt(row, 0).toString());
                    bookTitleField.setText(bookTableModel.getValueAt(row, 1).toString());
                    bookTypeField.setText(bookTableModel.getValueAt(row, 2).toString());
                }
            }
        });
        panel.add(new JScrollPane(bookTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Kelola Buku"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        bookCodeField = new JTextField(16);
        bookTitleField = new JTextField(20);
        bookTypeField = new JTextField(20);
        bookSearchField = new JTextField(16);

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Kode"), gbc);
        gbc.gridx = 1;
        form.add(bookCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Judul"), gbc);
        gbc.gridx = 1;
        form.add(bookTitleField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Jenis"), gbc);
        gbc.gridx = 1;
        form.add(bookTypeField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Tambah");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Hapus");
        JButton clearBtn = new JButton("Bersihkan");
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton searchBtn = new JButton("Cari");
        JButton resetBtn = new JButton("Reset");
        searchPanel.add(new JLabel("Kata kunci:"));
        searchPanel.add(bookSearchField);
        searchPanel.add(searchBtn);
        searchPanel.add(resetBtn);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        form.add(searchPanel, gbc);

        panel.add(form, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> handleAddBook());
        updateBtn.addActionListener(e -> handleUpdateBook());
        deleteBtn.addActionListener(e -> handleDeleteBook());
        clearBtn.addActionListener(e -> clearBookForm());
        searchBtn.addActionListener(e -> handleSearchBook());
        resetBtn.addActionListener(e -> refreshBookTable(loadBooks()));

        refreshBookTable(loadBooks());
        return panel;
    }

    private JPanel buildEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());

        employeeTableModel = new DefaultTableModel(new String[]{"NIP", "Nama", "Tgl Lahir"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        employeeTable = new JTable(employeeTableModel);
        employeeTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = employeeTable.getSelectedRow();
                if (row >= 0) {
                    employeeNipField.setText(employeeTableModel.getValueAt(row, 0).toString());
                    employeeNameField.setText(employeeTableModel.getValueAt(row, 1).toString());
                    employeeBirthDateField.setText(employeeTableModel.getValueAt(row, 2).toString());
                }
            }
        });
        panel.add(new JScrollPane(employeeTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Kelola Pegawai"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        employeeNipField = new JTextField(16);
        employeeNameField = new JTextField(20);
        employeeBirthDateField = new JTextField(12);

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("NIP"), gbc);
        gbc.gridx = 1;
        form.add(employeeNipField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("Nama"), gbc);
        gbc.gridx = 1;
        form.add(employeeNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Tgl Lahir (yyyy-MM-dd)"), gbc);
        gbc.gridx = 1;
        form.add(employeeBirthDateField, gbc);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton addBtn = new JButton("Tambah");
        JButton updateBtn = new JButton("Update");
        JButton deleteBtn = new JButton("Hapus");
        JButton clearBtn = new JButton("Bersihkan");
        buttons.add(addBtn);
        buttons.add(updateBtn);
        buttons.add(deleteBtn);
        buttons.add(clearBtn);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        form.add(buttons, gbc);

        panel.add(form, BorderLayout.SOUTH);

        addBtn.addActionListener(e -> handleAddEmployee());
        updateBtn.addActionListener(e -> handleUpdateEmployee());
        deleteBtn.addActionListener(e -> handleDeleteEmployee());
        clearBtn.addActionListener(e -> clearEmployeeForm());

        refreshEmployeeTable(loadEmployees());
        return panel;
    }

    private JPanel buildTransactionPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        transactionTableModel = new DefaultTableModel(new String[]{"Kode", "NIS", "Kode Buku", "Pinjam", "Kembali", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        transactionTable = new JTable(transactionTableModel);
        panel.add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Transaksi"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 4, 4, 4);
        gbc.anchor = GridBagConstraints.WEST;

        trxCodeField = new JTextField(10);
        trxNisField = new JTextField(10);
        trxBookCodeField = new JTextField(10);
        trxBorrowDateField = new JTextField(10);
        trxReturnDateField = new JTextField(10);
        trxReturnCodeField = new JTextField(10);

        gbc.gridx = 0;
        gbc.gridy = 0;
        form.add(new JLabel("Kode Transaksi"), gbc);
        gbc.gridx = 1;
        form.add(trxCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        form.add(new JLabel("NIS"), gbc);
        gbc.gridx = 1;
        form.add(trxNisField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Kode Buku"), gbc);
        gbc.gridx = 1;
        form.add(trxBookCodeField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        form.add(new JLabel("Tgl Pinjam (yyyy-MM-dd)"), gbc);
        gbc.gridx = 1;
        form.add(trxBorrowDateField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        form.add(new JLabel("Tgl Kembali (yyyy-MM-dd)"), gbc);
        gbc.gridx = 1;
        form.add(trxReturnDateField, gbc);

        JButton borrowBtn = new JButton("Simpan Peminjaman");
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        form.add(borrowBtn, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 2;
        gbc.gridy = 0;
        form.add(new JLabel("Kode Transaksi (Kembali)"), gbc);
        gbc.gridx = 3;
        form.add(trxReturnCodeField, gbc);

        JButton returnBtn = new JButton("Proses Pengembalian");
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        form.add(returnBtn, gbc);

        panel.add(form, BorderLayout.SOUTH);

        borrowBtn.addActionListener(e -> handleBorrowTransaction());
        returnBtn.addActionListener(e -> handleReturnTransaction());

        refreshTransactionTable(loadTransactions());
        return panel;
    }

    private JPanel buildReportPanel() {
        JPanel panel = new JPanel(new BorderLayout());

        JPanel top = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton notReturnedBtn = new JButton("Buku Belum Kembali");
        JButton overdueBtn = new JButton("Peminjam Terlambat");
        JButton historyBtn = new JButton("Riwayat Peminjaman");
        JButton totalStudentsBtn = new JButton("Total Siswa");
        JButton totalBooksBtn = new JButton("Total Buku");
        top.add(notReturnedBtn);
        top.add(overdueBtn);
        top.add(historyBtn);
        top.add(totalStudentsBtn);
        top.add(totalBooksBtn);

        reportTableModel = new DefaultTableModel(new String[]{"Kode", "Siswa", "Buku", "Batas Kembali", "Status"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        reportTable = new JTable(reportTableModel);
        reportSummaryLabel = new JLabel(" ");
        reportSummaryLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        panel.add(top, BorderLayout.NORTH);
        panel.add(new JScrollPane(reportTable), BorderLayout.CENTER);
        panel.add(reportSummaryLabel, BorderLayout.SOUTH);

        notReturnedBtn.addActionListener(e -> showBooksNotReturned());
        overdueBtn.addActionListener(e -> showOverdueBorrowers());
        historyBtn.addActionListener(e -> showBorrowHistory());
        totalStudentsBtn.addActionListener(e -> showTotalStudents());
        totalBooksBtn.addActionListener(e -> showTotalBooks());

        return panel;
    }

    private void handleAddStudent() {
        String nis = studentNisField.getText().trim();
        String name = sanitizeField(studentNameField.getText());
        String address = sanitizeField(studentAddressField.getText());

        if (nis.isEmpty() || name.isEmpty() || address.isEmpty()) {
            showMessage("Semua field siswa wajib diisi.");
            return;
        }

        List<Student> students = loadStudents();
        if (findStudentByNis(students, nis) != null) {
            showMessage("NIS sudah terdaftar.");
            return;
        }

        students.add(new Student(nis, name, address));
        saveStudents(students);
        refreshStudentTable(students);
        clearStudentForm();
    }

    private void handleUpdateStudent() {
        String nis = studentNisField.getText().trim();
        String name = sanitizeField(studentNameField.getText());
        String address = sanitizeField(studentAddressField.getText());

        if (nis.isEmpty() || name.isEmpty() || address.isEmpty()) {
            showMessage("Semua field siswa wajib diisi.");
            return;
        }

        List<Student> students = loadStudents();
        Student target = findStudentByNis(students, nis);
        if (target == null) {
            showMessage("Siswa tidak ditemukan.");
            return;
        }

        target.setName(name);
        target.setAddress(address);
        saveStudents(students);
        refreshStudentTable(students);
        clearStudentForm();
    }

    private void handleDeleteStudent() {
        String nis = studentNisField.getText().trim();
        if (nis.isEmpty()) {
            showMessage("Pilih siswa terlebih dahulu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Yakin hapus data siswa?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        List<Student> students = loadStudents();
        Student target = findStudentByNis(students, nis);
        if (target == null) {
            showMessage("Siswa tidak ditemukan.");
            return;
        }

        students.remove(target);
        saveStudents(students);
        refreshStudentTable(students);
        clearStudentForm();
    }

    private void handleSearchStudent() {
        String keyword = studentSearchField.getText().trim().toLowerCase();
        List<Student> students = loadStudents();
        if (keyword.isEmpty()) {
            refreshStudentTable(students);
            return;
        }

        List<Student> results = new ArrayList<>();
        for (Student student : students) {
            String full = (student.getNis() + " " + student.getName() + " " + student.getAddress()).toLowerCase();
            if (full.contains(keyword)) {
                results.add(student);
            }
        }
        refreshStudentTable(results);
    }

    private void clearStudentForm() {
        studentNisField.setText("");
        studentNameField.setText("");
        studentAddressField.setText("");
    }

    private void handleAddBook() {
        String code = bookCodeField.getText().trim();
        String title = sanitizeField(bookTitleField.getText());
        String type = sanitizeField(bookTypeField.getText());

        if (code.isEmpty() || title.isEmpty() || type.isEmpty()) {
            showMessage("Semua field buku wajib diisi.");
            return;
        }

        List<Book> books = loadBooks();
        if (findBookByCode(books, code) != null) {
            showMessage("Kode buku sudah terdaftar.");
            return;
        }

        books.add(new Book(code, title, type));
        saveBooks(books);
        refreshBookTable(books);
        clearBookForm();
    }

    private void handleUpdateBook() {
        String code = bookCodeField.getText().trim();
        String title = sanitizeField(bookTitleField.getText());
        String type = sanitizeField(bookTypeField.getText());

        if (code.isEmpty() || title.isEmpty() || type.isEmpty()) {
            showMessage("Semua field buku wajib diisi.");
            return;
        }

        List<Book> books = loadBooks();
        Book target = findBookByCode(books, code);
        if (target == null) {
            showMessage("Buku tidak ditemukan.");
            return;
        }

        target.setTitle(title);
        target.setType(type);
        saveBooks(books);
        refreshBookTable(books);
        clearBookForm();
    }

    private void handleDeleteBook() {
        String code = bookCodeField.getText().trim();
        if (code.isEmpty()) {
            showMessage("Pilih buku terlebih dahulu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Yakin hapus data buku?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        List<Book> books = loadBooks();
        Book target = findBookByCode(books, code);
        if (target == null) {
            showMessage("Buku tidak ditemukan.");
            return;
        }

        books.remove(target);
        saveBooks(books);
        refreshBookTable(books);
        clearBookForm();
    }

    private void handleSearchBook() {
        String keyword = bookSearchField.getText().trim().toLowerCase();
        List<Book> books = loadBooks();
        if (keyword.isEmpty()) {
            refreshBookTable(books);
            return;
        }

        List<Book> results = new ArrayList<>();
        for (Book book : books) {
            String full = (book.getCode() + " " + book.getTitle() + " " + book.getType()).toLowerCase();
            if (full.contains(keyword)) {
                results.add(book);
            }
        }
        refreshBookTable(results);
    }

    private void clearBookForm() {
        bookCodeField.setText("");
        bookTitleField.setText("");
        bookTypeField.setText("");
    }

    private void handleAddEmployee() {
        String nip = employeeNipField.getText().trim();
        String name = sanitizeField(employeeNameField.getText());
        String birthDate = employeeBirthDateField.getText().trim();

        if (nip.isEmpty() || name.isEmpty() || birthDate.isEmpty()) {
            showMessage("Semua field pegawai wajib diisi.");
            return;
        }

        if (!isValidDate(birthDate)) {
            showMessage("Format tanggal salah. Gunakan yyyy-MM-dd.");
            return;
        }

        List<Employee> employees = loadEmployees();
        if (findEmployeeByNip(employees, nip) != null) {
            showMessage("NIP sudah terdaftar.");
            return;
        }

        employees.add(new Employee(nip, name, birthDate));
        saveEmployees(employees);
        refreshEmployeeTable(employees);
        clearEmployeeForm();
    }

    private void handleUpdateEmployee() {
        String nip = employeeNipField.getText().trim();
        String name = sanitizeField(employeeNameField.getText());
        String birthDate = employeeBirthDateField.getText().trim();

        if (nip.isEmpty() || name.isEmpty() || birthDate.isEmpty()) {
            showMessage("Semua field pegawai wajib diisi.");
            return;
        }

        if (!isValidDate(birthDate)) {
            showMessage("Format tanggal salah. Gunakan yyyy-MM-dd.");
            return;
        }

        List<Employee> employees = loadEmployees();
        Employee target = findEmployeeByNip(employees, nip);
        if (target == null) {
            showMessage("Pegawai tidak ditemukan.");
            return;
        }

        target.setName(name);
        target.setBirthDate(birthDate);
        saveEmployees(employees);
        refreshEmployeeTable(employees);
        clearEmployeeForm();
    }

    private void handleDeleteEmployee() {
        String nip = employeeNipField.getText().trim();
        if (nip.isEmpty()) {
            showMessage("Pilih pegawai terlebih dahulu.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(frame, "Yakin hapus data pegawai?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        List<Employee> employees = loadEmployees();
        Employee target = findEmployeeByNip(employees, nip);
        if (target == null) {
            showMessage("Pegawai tidak ditemukan.");
            return;
        }

        employees.remove(target);
        saveEmployees(employees);
        refreshEmployeeTable(employees);
        clearEmployeeForm();
    }

    private void clearEmployeeForm() {
        employeeNipField.setText("");
        employeeNameField.setText("");
        employeeBirthDateField.setText("");
    }

    private void handleBorrowTransaction() {
        String trxCode = trxCodeField.getText().trim();
        String nis = trxNisField.getText().trim();
        String bookCode = trxBookCodeField.getText().trim();
        String borrowDate = trxBorrowDateField.getText().trim();
        String returnDate = trxReturnDateField.getText().trim();

        if (trxCode.isEmpty() || nis.isEmpty() || bookCode.isEmpty() || borrowDate.isEmpty() || returnDate.isEmpty()) {
            showMessage("Semua field peminjaman wajib diisi.");
            return;
        }

        if (!isValidDate(borrowDate) || !isValidDate(returnDate)) {
            showMessage("Format tanggal salah. Gunakan yyyy-MM-dd.");
            return;
        }

        LocalDate borrowLocal = LocalDate.parse(borrowDate, DATE_FORMATTER);
        LocalDate returnLocal = LocalDate.parse(returnDate, DATE_FORMATTER);
        if (returnLocal.isBefore(borrowLocal)) {
            showMessage("Tanggal kembali tidak boleh lebih awal dari tanggal pinjam.");
            return;
        }

        List<Transaction> transactions = loadTransactions();
        if (findTransactionByCode(transactions, trxCode) != null) {
            showMessage("Kode transaksi sudah ada.");
            return;
        }

        List<Student> students = loadStudents();
        List<Book> books = loadBooks();

        Student student = findStudentByNis(students, nis);
        if (student == null) {
            showMessage("NIS tidak ditemukan.");
            return;
        }

        if (countActiveBorrowByNis(transactions, nis) >= 2) {
            showMessage("Siswa sudah meminjam maksimal 2 buku aktif.");
            return;
        }

        Book book = findBookByCode(books, bookCode);
        if (book == null) {
            showMessage("Kode buku tidak ditemukan.");
            return;
        }

        if (isBookCurrentlyBorrowed(transactions, bookCode)) {
            showMessage("Buku sedang dipinjam dan belum dikembalikan.");
            return;
        }

        transactions.add(new Transaction(trxCode, nis, bookCode, borrowDate, returnDate, 0));
        saveTransactions(transactions);
        refreshTransactionTable(transactions);
        clearTransactionForm();
    }

    private void handleReturnTransaction() {
        String trxCode = trxReturnCodeField.getText().trim();
        if (trxCode.isEmpty()) {
            showMessage("Masukkan kode transaksi.");
            return;
        }

        List<Transaction> transactions = loadTransactions();
        Transaction target = findTransactionByCode(transactions, trxCode);
        if (target == null) {
            showMessage("Transaksi tidak ditemukan.");
            return;
        }

        if (target.getStatus() == 1) {
            showMessage("Buku pada transaksi ini sudah dikembalikan.");
            return;
        }

        target.setStatus(1);
        saveTransactions(transactions);
        refreshTransactionTable(transactions);
    }

    private void clearTransactionForm() {
        trxCodeField.setText("");
        trxNisField.setText("");
        trxBookCodeField.setText("");
        trxBorrowDateField.setText("");
        trxReturnDateField.setText("");
        trxReturnCodeField.setText("");
    }

    private void showBooksNotReturned() {
        List<Transaction> transactions = loadTransactions();
        List<Student> students = loadStudents();
        List<Book> books = loadBooks();

        reportTableModel.setRowCount(0);
        reportSummaryLabel.setText(" ");

        boolean found = false;
        for (Transaction trx : transactions) {
            if (trx.getStatus() == 0) {
                found = true;
                reportTableModel.addRow(new Object[]{
                        trx.getTransactionCode(),
                        getStudentNameByNis(students, trx.getNis()),
                        getBookTitleByCode(books, trx.getBookCode()),
                        trx.getReturnDate(),
                        "Belum"
                });
            }
        }

        if (!found) {
            reportSummaryLabel.setText("Tidak ada buku yang belum dikembalikan.");
        }
    }

    private void showOverdueBorrowers() {
        List<Transaction> transactions = loadTransactions();
        List<Student> students = loadStudents();
        List<Book> books = loadBooks();

        reportTableModel.setRowCount(0);
        reportSummaryLabel.setText(" ");

        LocalDate today = LocalDate.now();
        boolean found = false;

        for (Transaction trx : transactions) {
            if (trx.getStatus() == 0) {
                try {
                    LocalDate returnDate = LocalDate.parse(trx.getReturnDate(), DATE_FORMATTER);
                    if (returnDate.isBefore(today)) {
                        found = true;
                        reportTableModel.addRow(new Object[]{
                                trx.getTransactionCode(),
                                getStudentNameByNis(students, trx.getNis()),
                                getBookTitleByCode(books, trx.getBookCode()),
                                trx.getReturnDate(),
                                "Terlambat"
                        });
                    }
                } catch (DateTimeParseException ignored) {
                    // abaikan tanggal invalid
                }
            }
        }

        if (!found) {
            reportSummaryLabel.setText("Tidak ada peminjam yang terlambat.");
        }
    }

    private void showBorrowHistory() {
        List<Transaction> transactions = loadTransactions();
        List<Student> students = loadStudents();
        List<Book> books = loadBooks();

        reportTableModel.setRowCount(0);
        reportSummaryLabel.setText(" ");

        if (transactions.isEmpty()) {
            reportSummaryLabel.setText("Belum ada transaksi peminjaman.");
            return;
        }

        for (Transaction trx : transactions) {
            reportTableModel.addRow(new Object[]{
                    trx.getTransactionCode(),
                    getStudentNameByNis(students, trx.getNis()),
                    getBookTitleByCode(books, trx.getBookCode()),
                    trx.getReturnDate(),
                    trx.getStatus() == 0 ? "Belum" : "Sudah"
            });
        }
    }

    private void showTotalStudents() {
        int total = loadStudents().size();
        reportTableModel.setRowCount(0);
        reportSummaryLabel.setText("Jumlah siswa: " + total);
    }

    private void showTotalBooks() {
        int total = loadBooks().size();
        reportTableModel.setRowCount(0);
        reportSummaryLabel.setText("Jumlah buku: " + total);
    }

    private void refreshStudentTable(List<Student> students) {
        studentTableModel.setRowCount(0);
        for (Student student : students) {
            studentTableModel.addRow(new Object[]{student.getNis(), student.getName(), student.getAddress()});
        }
    }

    private void refreshBookTable(List<Book> books) {
        bookTableModel.setRowCount(0);
        for (Book book : books) {
            bookTableModel.addRow(new Object[]{book.getCode(), book.getTitle(), book.getType()});
        }
    }

    private void refreshEmployeeTable(List<Employee> employees) {
        employeeTableModel.setRowCount(0);
        for (Employee employee : employees) {
            employeeTableModel.addRow(new Object[]{employee.getNip(), employee.getName(), employee.getBirthDate()});
        }
    }

    private void refreshTransactionTable(List<Transaction> transactions) {
        transactionTableModel.setRowCount(0);
        for (Transaction trx : transactions) {
            transactionTableModel.addRow(new Object[]{
                    trx.getTransactionCode(),
                    trx.getNis(),
                    trx.getBookCode(),
                    trx.getBorrowDate(),
                    trx.getReturnDate(),
                    trx.getStatus() == 0 ? "Belum" : "Sudah"
            });
        }
    }

    private void initializeDataFiles() {
        FileHelper.ensureFileExists(STUDENT_FILE);
        FileHelper.ensureFileExists(BOOK_FILE);
        FileHelper.ensureFileExists(EMPLOYEE_FILE);
        FileHelper.ensureFileExists(TRANSACTION_FILE);

        List<String> dummyStudents = new ArrayList<>();
        dummyStudents.add("231001|Budi Santoso|Jl. Mawar No. 12");
        dummyStudents.add("231002|Siti Aminah|Jl. Melati No. 8");
        dummyStudents.add("231003|Raka Pratama|Jl. Kenanga No. 5");

        List<String> dummyBooks = new ArrayList<>();
        dummyBooks.add("BK001|Matematika Kelas 9|Pelajaran");
        dummyBooks.add("BK002|IPA Terpadu|Pelajaran");
        dummyBooks.add("BK003|Laskar Pelangi|Novel");

        List<String> dummyEmployees = new ArrayList<>();
        dummyEmployees.add("255150207111059|ARYAN ZAKY PRAYOGO|2004-07-10");
        dummyEmployees.add("255150200111042|ACHMAD HUJAIRI|2004-02-22");
        dummyEmployees.add("255150201111025|M. HIDAYATULLOH H. A. M|2004-05-19");
        dummyEmployees.add("255150200111041|M. AHSHAL ZILHAMSYAH|2004-09-15");
        dummyEmployees.add("255150200111040|DIKARDO SIAHAAN|2004-12-01");

        FileHelper.initializeWithDummyDataIfEmpty(STUDENT_FILE, dummyStudents);
        FileHelper.initializeWithDummyDataIfEmpty(BOOK_FILE, dummyBooks);
        FileHelper.initializeWithDummyDataIfEmpty(EMPLOYEE_FILE, dummyEmployees);
    }

    private List<Student> loadStudents() {
        List<String> lines = FileHelper.readLines(STUDENT_FILE);
        List<Student> students = new ArrayList<>();
        for (String line : lines) {
            Student student = Student.fromLine(line);
            if (student != null) {
                students.add(student);
            }
        }
        return students;
    }

    private void saveStudents(List<Student> students) {
        List<String> lines = new ArrayList<>();
        for (Student student : students) {
            lines.add(student.toLine());
        }
        FileHelper.writeLines(STUDENT_FILE, lines);
    }

    private List<Book> loadBooks() {
        List<String> lines = FileHelper.readLines(BOOK_FILE);
        List<Book> books = new ArrayList<>();
        for (String line : lines) {
            Book book = Book.fromLine(line);
            if (book != null) {
                books.add(book);
            }
        }
        return books;
    }

    private void saveBooks(List<Book> books) {
        List<String> lines = new ArrayList<>();
        for (Book book : books) {
            lines.add(book.toLine());
        }
        FileHelper.writeLines(BOOK_FILE, lines);
    }

    private List<Employee> loadEmployees() {
        List<String> lines = FileHelper.readLines(EMPLOYEE_FILE);
        List<Employee> employees = new ArrayList<>();
        for (String line : lines) {
            Employee employee = Employee.fromLine(line);
            if (employee != null) {
                employees.add(employee);
            }
        }
        return employees;
    }

    private void saveEmployees(List<Employee> employees) {
        List<String> lines = new ArrayList<>();
        for (Employee employee : employees) {
            lines.add(employee.toLine());
        }
        FileHelper.writeLines(EMPLOYEE_FILE, lines);
    }

    private List<Transaction> loadTransactions() {
        List<String> lines = FileHelper.readLines(TRANSACTION_FILE);
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            Transaction transaction = Transaction.fromLine(line);
            if (transaction != null) {
                transactions.add(transaction);
            }
        }
        return transactions;
    }

    private void saveTransactions(List<Transaction> transactions) {
        List<String> lines = new ArrayList<>();
        for (Transaction transaction : transactions) {
            lines.add(transaction.toLine());
        }
        FileHelper.writeLines(TRANSACTION_FILE, lines);
    }

    private Employee findEmployeeByCredentials(String nip, String name) {
        List<Employee> employees = loadEmployees();
        for (Employee employee : employees) {
            if (employee.getNip().equalsIgnoreCase(nip.trim())
                    && employee.getName().equalsIgnoreCase(name.trim())) {
                return employee;
            }
        }
        return null;
    }

    private Student findStudentByNis(List<Student> students, String nis) {
        for (Student student : students) {
            if (student.getNis().equalsIgnoreCase(nis.trim())) {
                return student;
            }
        }
        return null;
    }

    private Book findBookByCode(List<Book> books, String code) {
        for (Book book : books) {
            if (book.getCode().equalsIgnoreCase(code.trim())) {
                return book;
            }
        }
        return null;
    }

    private Employee findEmployeeByNip(List<Employee> employees, String nip) {
        for (Employee employee : employees) {
            if (employee.getNip().equalsIgnoreCase(nip.trim())) {
                return employee;
            }
        }
        return null;
    }

    private Transaction findTransactionByCode(List<Transaction> transactions, String transactionCode) {
        for (Transaction transaction : transactions) {
            if (transaction.getTransactionCode().equalsIgnoreCase(transactionCode.trim())) {
                return transaction;
            }
        }
        return null;
    }

    private long countActiveBorrowByNis(List<Transaction> transactions, String nis) {
        long count = 0;
        for (Transaction transaction : transactions) {
            if (transaction.getNis().equalsIgnoreCase(nis.trim()) && transaction.getStatus() == 0) {
                count++;
            }
        }
        return count;
    }

    private boolean isBookCurrentlyBorrowed(List<Transaction> transactions, String bookCode) {
        for (Transaction transaction : transactions) {
            if (transaction.getBookCode().equalsIgnoreCase(bookCode.trim()) && transaction.getStatus() == 0) {
                return true;
            }
        }
        return false;
    }

    private String getStudentNameByNis(List<Student> students, String nis) {
        Student student = findStudentByNis(students, nis);
        return student != null ? student.getName() : "Siswa tidak ditemukan";
    }

    private String getBookTitleByCode(List<Book> books, String code) {
        Book book = findBookByCode(books, code);
        return book != null ? book.getTitle() : "Buku tidak ditemukan";
    }

    private String sanitizeField(String value) {
        if (value == null) {
            return "";
        }
        String clean = value.trim();
        return clean.replace("|", "/");
    }

    private boolean isValidDate(String value) {
        try {
            LocalDate.parse(value, DATE_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }
}
