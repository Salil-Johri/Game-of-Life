/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package johri_gameoflifegui;

/**
 *
 * @author salil
 */
public class Life implements LifeInterface {
    int[][] cells;
    public Life(int length) {
        cells = new int[length][length];
        for (int i = 0; i < (cells.length); i++) {
            for (int j = 0; j < (cells[0].length); j++) {
                cells[i][j] = 0;
            }
        }
    }
    
    public Life(int [][] startGrid) {
        cells = new int[startGrid.length][startGrid[0].length];
        for (int i = 0; i < cells.length; i++) {
            System.arraycopy(startGrid[i], 0, cells[i], 0, cells[0].length);
        }
       
    }
    
    @Override
    public void killAllCells() {
        for (int i = 0; i < (cells.length); i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = 0;
            }
        }
    }

    @Override
    public void setPattern(int[][] startGrid) {
        cells = new int [startGrid.length][startGrid[0].length];
        for (int i = 0; i < cells.length; i++) {
            System.arraycopy(startGrid[i], 0, cells[i], 0, cells.length);
        }
    }

    @Override
    public int countNeighbours(int cellRow, int cellCol) {
        int count = 0;
        if (cells[cellRow][cellCol] == 1) {
            count = -1;
        }
        for (int i = cellRow - 1; i <= cellRow + 1; i++) {
            for (int j = cellCol - 1; j <= cellCol + 1; j++) {
                try {
                    if(cells[i][j] == 1) {
                    count ++;
                    }
                }catch(ArrayIndexOutOfBoundsException e) {
                        
                        }

                }
            }

        return count;
    }

    @Override
    public int applyRules(int cellRow, int cellCol) {
        int aliveOrDead = 0;
        if (cells[cellRow][cellCol] == 1) {
            if (countNeighbours(cellRow, cellCol) > 3) {
                aliveOrDead = 0;
            } else if (countNeighbours(cellRow, cellCol) == 2 || countNeighbours(cellRow, cellCol) == 3) {
                aliveOrDead = 1;
            } else {
                aliveOrDead = 0;
            }
        
        } else {
            if(countNeighbours(cellRow, cellCol) == 3) {
                aliveOrDead = 1;
            }
        }
        
        return aliveOrDead;
    }

    @Override
    public int [][] takeStep(int [][] nG) {
        int newGrid[][] = new int[cells.length][cells[0].length];
        for (int i = 0; i < cells.length; i++) {
            System.arraycopy(nG[i], 0, newGrid[i], 0, cells[0].length);
        }
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                newGrid[i][j] = applyRules(i, j);
            }
        }
        
        for (int i = 0; i < cells.length; i++) {
            System.arraycopy(newGrid[i], 0, cells[i], 0, cells[0].length);
        }
        
        return newGrid;
    }

    @Override
    public String toString() {
        String temp = "";
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                temp += cells[i][j];
            }
            temp += "\n";
        }
        
        return temp;
    }
}

