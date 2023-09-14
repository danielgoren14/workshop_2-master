import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class Window extends JFrame {
    private final Square[][] squares;

    public Window() {
        initializeWindowProperties();
        squares = createBoardSquares();
        addBoardPanelToWindow();
        addButtonsToWindow();
        setBoardInitialStatusFromApi();
        packAndSetWindowPosition();
    }

    private void initializeWindowProperties() {
        setTitle(Constants.WINDOW_TITLE);
        setSize(Constants.WINDOW_SIZE, Constants.WINDOW_SIZE);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
    }

    private Square[][] createBoardSquares() {
        Square[][] boardSquares = new Square[Constants.BOARD_SIZE][Constants.BOARD_SIZE];
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                boardSquares[i][j] = new Square(this, i, j);
            }
        }
        return boardSquares;
    }

    private void addBoardPanelToWindow() {
        JPanel boardPanel = new JPanel(new GridLayout(Constants.BOARD_SIZE, Constants.BOARD_SIZE));
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                boardPanel.add(squares[i][j]);
            }
        }
        add(boardPanel, BorderLayout.CENTER);
    }

    private void addClearButtonToWindow() {
        JButton clearButton = new JButton(Constants.CLEAR_BOARD_TEXT);
        clearButton.addActionListener(e -> clearBoard());
        add(clearButton, BorderLayout.SOUTH);
    }

    private void clearBoard() {
        ApiUtil.clearBoard();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            for (int j = 0; j < Constants.BOARD_SIZE; j++) {
                squares[i][j].setBackground(null);
                squares[i][j].setEnabled(true);
            }
        }
    }

    private void setBoardInitialStatusFromApi() {
        Character[][] board = ApiUtil.getBoard();
        for (int i = 0; i < Objects.requireNonNull(board).length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                setSquareStatusFromCharacter(i, j, board[i][j]);
            }
        }
    }

    private void setSquareStatusFromCharacter(int i, int j, Character character) {
        if (character != Constants.EMPTY_SQUARE) {
            if (character.equals('X')) {
                squares[i][j].setBackground(Color.BLUE);
            } else {
                squares[i][j].setBackground(Color.BLACK);
            }
            squares[i][j].setEnabled(false);
        }
    }

    private void packAndSetWindowPosition() {
        pack();
        setLocationRelativeTo(null);
    }

    public Square getSquare(int position) {
        return squares[position / Constants.BOARD_SIZE][position % Constants.BOARD_SIZE];
    }

    public Character[][] convertSquaresToMatrix() {
        int rows = squares.length;
        int cols = squares[0].length;
        Character[][] result = new Character[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[i][j] = getCharacterFromSquareColor(squares[i][j].getBackground());
            }
        }
        return result;
    }

    private Character getCharacterFromSquareColor(Color color) {
        if (color.equals(Color.BLUE)) {
            return Constants.PLAYER_SYMBOL;
        } else if (color.equals(Color.BLACK)) {
            return Constants.RIVAL_SYMBOL;
        } else {
            return Constants.EMPTY_SQUARE;
        }
    }

    public void enableDisableEmptySquares(boolean enable) {
        for (int i = 0; i < squares.length; i++) {
            for (int j = 0; j < squares[i].length; j++) {
                squares[i][j].setEnabled(enable);
            }
        }
    }

    private JButton createCheckWinnerButton() {
        JButton checkWinnerButton = new JButton("Check Winner");
        checkWinnerButton.addActionListener(e -> checkWinner());
        return checkWinnerButton;
    }

    private void checkWinner() {
        if (ApiUtil.checkWinner()) {
            JOptionPane.showMessageDialog(null, "Success!");
        } else {
            JOptionPane.showMessageDialog(null, "Failure! Clear the board and try again");
        }
    }

    private void addButtonsToWindow() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JButton clearButton = new JButton(Constants.CLEAR_BOARD_TEXT);
        clearButton.addActionListener(e -> clearBoard());
        clearButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, clearButton.getPreferredSize().height));
        clearButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        JButton checkWinnerButton = createCheckWinnerButton();
        checkWinnerButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, checkWinnerButton.getPreferredSize().height));
        checkWinnerButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        buttonPanel.add(clearButton);
        buttonPanel.add(checkWinnerButton);

        add(buttonPanel, BorderLayout.SOUTH);
    }



}