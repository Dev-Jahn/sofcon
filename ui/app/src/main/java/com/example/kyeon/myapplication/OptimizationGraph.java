package com.example.kyeon.myapplication;

public class OptimizationGraph {
    private int n;           //노드들의 수
    private int maps[][];    //노드들간의 가중치 저장할 변수

    public OptimizationGraph(int n) {
        this.n = n;
        maps = new int[n + 1][n + 1];

    }

    public void input(int i, int j, int w) {
        maps[i][j] = w;
        maps[j][i] = w;
    }

    public void dijkstra(int v) {
        int distance[] = new int[n + 1];          //최단 거리를 저장할 변수
        boolean[] check = new boolean[n + 1];     //해당 노드를 방문했는지 체크할 변수

        //distance값 초기화.
        for (int i = 1; i < n + 1; i++) {
            distance[i] = Integer.MAX_VALUE;
        }

        //시작노드값 초기화.
        distance[v] = 0;
        check[v] = true;

        //연결노드 distance갱신
        for (int i = 1; i < n + 1; i++) {
            if (!check[i] && maps[v][i] != 0) {
                distance[i] = maps[v][i];
            }
        }


        for (int i = 0; i < n - 1; i++) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;

            //최소값 찾기
            for (int j = 1; j < n + 1; j++) {
                if (!check[j] && distance[j] != Integer.MAX_VALUE) {
                    if (distance[j] < min) {
                        min = distance[j];
                        minIndex = j;
                    }
                }
            }
            check[minIndex] = true;
            for (int j = 1; j < n + 1; j++) {
                if (!check[j] && maps[minIndex][j] != 0) {
                    if (distance[j] > distance[minIndex] + maps[minIndex][j]) {
                        distance[j] = distance[minIndex] + maps[minIndex][j];
                    }
                }
            }

        }
        //결과값 출력
        for (int i = 1; i < n + 1; i++) {
            System.out.print(distance[i] + " ");
        }
        System.out.println("");

    }
}
