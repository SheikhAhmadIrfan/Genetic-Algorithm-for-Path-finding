package genetic;
/**
 *
 * @author ahmad
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Genetic {
    private static final int GRID_SIZE = 30;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI(GRID_SIZE));
    }

    private static void createAndShowGUI(int gridSize) {
        JFrame frame = new JFrame("Star");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 700);
        StarPanel starPanel = new StarPanel(gridSize);
        frame.add(starPanel);
        frame.setVisible(true);
    }
}

class StarPanel extends JPanel {
    private final int nodeSize = 15;
    private final Color backgroundColor = Color.BLACK;
    private final Color nodeColor = Color.WHITE;
    NodePanel[][] n = new NodePanel[30][30];
    NodePanel start, end, block, current;
    ArrayList<NodePanel> openList  =new ArrayList<>();
    ArrayList<NodePanel> closeList  =new ArrayList<>();
    boolean goalReached=false;
    int c;
    

    public StarPanel(int gridSize) {
        this.setBackground(backgroundColor);
        this.setLayout(new GridLayout(gridSize, gridSize));
        Random random = new Random();
        this.addKeyListener(new KeyHandler(this));
        this.setFocusable(true);
        
        for (int col = 0; col < gridSize; col++) {
            for (int row = 0; row < gridSize; row++) {
                n[col][row] = new NodePanel(col, row);
                this.add(n[col][row]);
                if (random.nextInt(100) < 10) {
                    block(col, row);
                }
            }
        }
        start(0, 0);
        end(25, 29);
        heuristic();
    }

    public void start(int col, int row) {
        n[col][row].startNode();
        start = n[col][row];
        current = start;
    }

    public void end(int col, int row) {
        n[col][row].endNode();
        end = n[col][row];
    }

    public void block(int col, int row) {
        n[col][row].blockNode();
        block = n[col][row];
    }
    public void heuristic() {
        for (int col = 0; col < 30; col++) {
            for (int row = 0; row < 30; row++) {
                int dx = Math.abs(n[col][row].col - end.col);
                int dy = Math.abs(n[col][row].row - end.row);
                n[col][row].hcost = (int) Math.sqrt(dx * dx + dy * dy);
                int da = Math.abs(n[col][row].col - start.col);
                int db = Math.abs(n[col][row].row - start.row);
                n[col][row].gcost = da + db;
                n[col][row].fcost = n[col][row].hcost + n[col][row].gcost;
                n[col][row].setMargin(new Insets(0, 0, 0, 0));
            }
        }
    }
    public void search(){
         while (!goalReached) {
            current.close();
            closeList.add(current);
            openList.remove(current);
            if(current.row-1>=0){
                if(n[current.col][current.row-1].open==false && n[current.col][current.row-1].close==false&&n[current.col][current.row-1].block==false){
                    n[current.col][current.row-1].open();
                    n[current.col][current.row-1].p = current;
                    openList.add(n[current.col][current.row-1]);
                }
            }
            if(current.row-1>=0 && current.col-1>=0){
                if(n[current.col-1][current.row-1].open==false && n[current.col-1][current.row-1].close==false&&n[current.col-1][current.row-1].block==false){
                    n[current.col-1][current.row-1].open();
                    n[current.col-1][current.row-1].p = current;
                    openList.add(n[current.col-1][current.row-1]);
                }
            }
            if(current.row-1>=0 && current.col+1<30){
                if(n[current.col+1][current.row-1].open==false && n[current.col+1][current.row-1].close==false&&n[current.col+1][current.row-1].block==false){
                    n[current.col+1][current.row-1].open();
                    n[current.col+1][current.row-1].p = current;
                    openList.add(n[current.col+1][current.row-1]);
                }
            }
            if(current.row+1<30 && current.col-1>=0){
                if(n[current.col-1][current.row+1].open==false && n[current.col-1][current.row+1].close==false&&n[current.col-1][current.row+1].block==false){
                    n[current.col-1][current.row+1].open();
                    n[current.col-1][current.row+1].p = current;
                    openList.add(n[current.col-1][current.row+1]);
                }
            }
            if(current.row+1<30 && current.col+1<30){
                if(n[current.col+1][current.row+1].open==false && n[current.col+1][current.row+1].close==false&&n[current.col+1][current.row+1].block==false){
                    n[current.col+1][current.row+1].open();
                    n[current.col+1][current.row+1].p = current;
                    openList.add(n[current.col+1][current.row+1]);
                }
            }
            if(current.col-1>=0){
                if(n[current.col-1][current.row].open==false && n[current.col-1][current.row].close==false&&n[current.col-1][current.row].block==false){
                    n[current.col-1][current.row].open();
                    n[current.col-1][current.row].p = current;
                    openList.add(n[current.col-1][current.row]);
                }
            }
            if(current.row+1<30){
                if(n[current.col][current.row+1].open==false && n[current.col][current.row+1].close==false&&n[current.col][current.row+1].block==false){
                    n[current.col][current.row+1].open();
                    n[current.col][current.row+1].p = current;
                    openList.add(n[current.col][current.row+1]);
                }
            }
            if(current.col+1<30){
                if(n[current.col+1][current.row].open==false && n[current.col+1][current.row].close==false&&n[current.col+1][current.row].block==false){
                    n[current.col+1][current.row].open();
                    n[current.col+1][current.row].p = current;
                    openList.add(n[current.col+1][current.row]);
                }
            }
            if (openList.isEmpty()) {
                for (int col = 0; col < 30; col++) {
                    for (int row = 0; row < 30; row++) {
                        n[col][row].noPath();
                    }
                }
                break;
            }
            int index=0;
            int max=99999;
            for(int i=0;i<openList.size();i++){
                if(openList.get(i).fcost < max){
                    index = i;
                    max=openList.get(i).fcost;      
                }
                else if(openList.get(i).fcost == max){
                    if(openList.get(i).gcost < openList.get(index).gcost){
                        index=i;
                    }
                }
            }
            current = openList.get(index);
            if(current == end){
                goalReached=true;
                NodePanel a=end;
                while(a!=start){
                    a=a.p;
                    if(a!=start){
                        a.path();
                    }
                }
            }
        }
    }
}

class NodePanel extends JButton{
    int col;
    int row;
    int hcost;
    int gcost;
    int fcost;
    NodePanel p;
    boolean start;
    boolean end;
    boolean block;
    boolean open;
    boolean close;
    public NodePanel(int col, int row) {
        this.col = col;
        this.row = row;
        setBackground(Color.white);
        setForeground(Color.black);
    }
    public void startNode() {
        setBackground(Color.blue);
        start = true;
    }
    public void noPath(){
        setBackground(Color.red);
    }
    public void endNode() {
        setBackground(Color.green);
        end = true;
    }
    public void blockNode() {
        setBackground(Color.black);
        block = true;
    }
    public void open(){
        open = true;
    }
    public void close(){
        close = true;
    }
    public void path(){
        setBackground(Color.gray);
    }
}


class KeyHandler implements KeyListener {
    StarPanel enterKeyListener;

    public KeyHandler(StarPanel enterKeyListener) {
        this.enterKeyListener = enterKeyListener;
    }
    @Override
    public void keyTyped(KeyEvent e) {}
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            enterKeyListener.search();
        }
    }
    @Override
    public void keyReleased(KeyEvent e) {}  

}

class Gene {
    private final int x;
    private final int y;

    Gene(final int x,final int y) {
        this.x = x;
        this.y = y;
    }
    int getX() {
        return this.x;
    }
    int getY() {
        return this.y;
    }
    double distance(final Gene other) {
        return sqrt(pow(getX() - other.getX(), 2) + pow(getY() - other.getY(), 2));
    }
}


class Chromosome {
    private final java.util.List<Gene> chromosome;
    private final double distance;

    public double getDistance() {
        return this.distance;
    }
    double calculateDistance() {
        double total = 0.0f;
        for(int i = 0; i < this.chromosome.size() - 1; i++) {
            total += this.chromosome.get(i).distance(this.chromosome.get(i+1));
        }
        return total;
    }
    private Chromosome(final java.util.List<Gene> chromosome) {
        this.chromosome = Collections.unmodifiableList(chromosome);
        this.distance = calculateDistance();
    }
    static Chromosome create(final Gene[] points) {
        final java.util.List<Gene> genes = Arrays.asList(Arrays.copyOf(points, points.length));
        Collections.shuffle(genes);
        return new Chromosome(genes);
    }
    java.util.List<Gene> getChromosome() {
        return this.chromosome;
    }
    Chromosome[] crossOver(final Chromosome other) {
        final java.util.List<Gene>[] myDNA = Utils.split(this.chromosome);
        final java.util.List<Gene>[] otherDNA = Utils.split(other.getChromosome());

        final java.util.List<Gene> firstCrossOver = new ArrayList<>(myDNA[0]);

        for(Gene gene : otherDNA[0]) {
            if(!firstCrossOver.contains(gene)) {
                firstCrossOver.add(gene);
            }
        }

        for(Gene gene : otherDNA[1]) {
            if(!firstCrossOver.contains(gene)) {
                firstCrossOver.add(gene);
            }
        }

        final java.util.List<Gene> secondCrossOver = new ArrayList<>(otherDNA[1]);

        for(Gene gene : myDNA[0]) {
            if(!secondCrossOver.contains(gene)) {
                secondCrossOver.add(gene);
            }
        }

        for(Gene gene : myDNA[1]) {
            if(!secondCrossOver.contains(gene)) {
                secondCrossOver.add(gene);
            }
        }

        if(firstCrossOver.size() != Utils.s.length || secondCrossOver.size() != Utils.s.length) {
            throw new RuntimeException("oops!");
        }
        return new Chromosome[]
        {
                new Chromosome(firstCrossOver),
                new Chromosome(secondCrossOver)
        };
    }
    Chromosome mutate() {
        final java.util.List<Gene> copy = new ArrayList<>(this.chromosome);
        int indexA = Utils.randomIndex(copy.size());
        int indexB = Utils.randomIndex(copy.size());
        while(indexA == indexB) {
            indexA = Utils.randomIndex(copy.size());
            indexB = Utils.randomIndex(copy.size());
        }
        Collections.swap(copy, indexA, indexB);
        return new Chromosome(copy);
    }
}

class Utils {
    private final static Random R = new Random(100);
    static final Gene[] s = generateData(8);
    private Utils() {
        throw new RuntimeException("No!");
    }
    private static Gene[] generateData(final int numDataPoints) {
        final Gene[] data = new Gene[numDataPoints];
        for(int i = 0; i < numDataPoints; i++) {
            data[i] = new Gene(Utils.randomIndex(30),Utils.randomIndex(30));
        }
        return data;
    }
    static int randomIndex(final int limit) {
        return R.nextInt(limit);
    }
    static<T> java.util.List<T>[] split(final java.util.List<T> list) {
        final java.util.List<T> first = new ArrayList<>();
        final java.util.List<T> second = new ArrayList<>();
        final int size = list.size();
        final int partitionIndex = 1 + Utils.randomIndex(list.size());
        IntStream.range(0, size).forEach(i -> {
            if(i < (size+1)/partitionIndex) {
                first.add(list.get(i));
            } else {
                second.add(list.get(i));
            }
        });
        return (java.util.List<T>[]) new java.util.List[] {first, second};
    }
}


class Population {

    private java.util.List<Chromosome> population;
    private final int initialSize;

    Population(final Gene[] points,final int initialSize) {
        this.population = init(points, initialSize);
        this.initialSize = initialSize;
    }

    java.util.List<Chromosome> getPopulation() {
        return this.population;
    }

    Chromosome getAlpha() {
        return this.population.get(0);
    }

    private java.util.List<Chromosome> init(final Gene[] points, final int initialSize) {
        final java.util.List<Chromosome> k = new ArrayList<>();
        for(int i = 0; i < initialSize; i++) {
            final Chromosome chromosome = Chromosome.create(points);
            k.add(chromosome);
        }
        return k;
    }

    private void doSelection() {
        this.population.sort(Comparator.comparingDouble(Chromosome::getDistance));
        this.population = this.population.stream().limit(this.initialSize).collect(Collectors.toList());
    }

    private void doSpawn() {
        IntStream.range(0, 100).forEach(e -> this.population.add(Chromosome.create(Utils.s)));
    }

    private void doMutation() {
        final java.util.List<Chromosome> newPopulation = new ArrayList<>();
        for(int i = 0; i < this.population.size()/10; i++) {
            final Chromosome mutation = this.population.get(Utils.randomIndex(this.population.size())).mutate();
            newPopulation.add(mutation);
        }
        this.population.addAll(newPopulation);
    }
    
    public void doGnes(){
        doCrossOver();
        doMutation();
        doSpawn();
        doSelection();
    
    }
    
    private void doCrossOver() {

        final java.util.List<Chromosome> newPopulation = new ArrayList<>();
        for(final Chromosome chromosome : this.population) {
            final Chromosome partner = getCrossOverPartner(chromosome);
            newPopulation.addAll(Arrays.asList(chromosome.crossOver(partner)));
        }
        this.population.addAll(newPopulation);
    }

    private Chromosome getCrossOverPartner(final Chromosome chromosome) {
        Chromosome partner = this.population.get(Utils.randomIndex(this.population.size()));
        while(chromosome == partner) {
            partner = this.population.get(Utils.randomIndex(this.population.size()));
        }
        return partner;
    }
}