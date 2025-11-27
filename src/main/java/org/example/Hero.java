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
        List<int[]> path = bfs(getX(), getY(), targetX, targetY);

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

    private List<int[]> bfs(int startX, int startY, int endX, int endY) {

        int h = map.getHeight();
        int w = map.getLenght();

        boolean[][] visited = new boolean[h][w];
        int[][][] parent = new int[h][w][2];

        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{startX, startY});
        visited[startX][startY] = true;

        int[][] dirs = {
                {1, 0},
                {-1, 0},
                {0, 1},
                {0, -1}
        };

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int x = cur[0], y = cur[1];

            if (x == endX && y == endY)
                break;

            for (int[] d : dirs) {
                int nx = x + d[0];
                int ny = y + d[1];

                if (nx < 0 || ny < 0 || nx >= h || ny >= w)
                    continue;

                if (!map.isWalkable(nx, ny))
                    continue;

                if (!visited[nx][ny]) {
                    visited[nx][ny] = true;
                    parent[nx][ny][0] = x;
                    parent[nx][ny][1] = y;
                    queue.add(new int[]{nx, ny});
                }
            }
        }

        if (!visited[endX][endY])
            return null;

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
