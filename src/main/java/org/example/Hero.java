package org.example;
import java.util.*;

public class Hero extends Unit {
    public Hero(int x, int y, Map map) {
        super(x, y, map);
    }

    @Override
    public void attack() {
        System.out.println("Герой наносит удар мечом!");
    }

    @Override
    public void move(int targetX, int targetY) {
        List<int[]> path = dijkstra(getX(), getY(), targetX, targetY);
        if (path == null) {
            System.out.println("Путь не найден!");
            return;
        }

        System.out.println("Двигаюсь по маршруту:");
        for (int[] p : path) {
            System.out.println("-> (" + p[0] + "," + p[1] + ")");
            setPosition(p[0], p[1]);
        }
    }

    private List<int[]> dijkstra(int startX, int startY, int endX, int endY) {
        int h = map.getHeight();
        int w = map.getLenght();

        int[][] dist = new int[h][w];
        int[][][] parent = new int[h][w][2];
        for (int[] row : dist) Arrays.fill(row, Integer.MAX_VALUE);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[0]));
        pq.add(new int[]{0, startX, startY});
        dist[startX][startY] = 0;

        int[][] dirs = {{1,0},{-1,0},{0,1},{0,-1}};

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int d = cur[0], x = cur[1], y = cur[2];

            if (x == endX && y == endY) break;

            for (int[] k : dirs) {
                int nx = x + k[0];
                int ny = y + k[1];

                if (nx < 0 || ny < 0 || nx >= h || ny >= w) continue;

                int cost = 1; // можно сделать зависимость от типа клетки

                if (dist[x][y] + cost < dist[nx][ny]) {
                    dist[nx][ny] = dist[x][y] + cost;
                    parent[nx][ny][0] = x;
                    parent[nx][ny][1] = y;
                    pq.add(new int[]{dist[nx][ny], nx, ny});
                }
            }
        }

        if (dist[endX][endY] == Integer.MAX_VALUE) return null;

        List<int[]> path = new ArrayList<>();
        int cx = endX, cy = endY;

        while (!(cx == startX && cy == startY)) {
            path.add(new int[]{cx, cy});
            int px = parent[cx][cy][0];
            int py = parent[cx][cy][1];
            cx = px;
            cy = py;
        }
        Collections.reverse(path);
        return path;
    }
}
