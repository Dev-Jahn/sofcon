package com.example.kyeon.myapplication;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class OptimizationGraph {
    private int n;
    private double maps[][];
    private int shortest;

    public OptimizationGraph(int n) {
        this.n = n;
        maps = new double[n + 1][n + 1];
    }

    public void input(int i, int j, double w) {
        maps[i][j] = w;
        maps[j][i] = w;
    }

    public int dijkstra(int v) {
        double distance[] = new double[n + 1];
        boolean[] check = new boolean[n + 1];

        for (int i = 1; i < n + 1; i++)
            distance[i] = Integer.MAX_VALUE;

        distance[v] = 0;
        check[v] = true;

        for (int i = 1; i < n + 1; i++) {
            if (!check[i] && maps[v][i] != 0) {
                distance[i] = maps[v][i];
            }
        }


        for (int i = 0; i < n - 1; i++) {
            double min = Double.MAX_VALUE;
            int minIndex = -1;

            for (int j = 1; j < n + 1; j++) {
                if (!check[j] && distance[j] != Double.MAX_VALUE) {
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
            min = Double.MAX_VALUE;
            minIndex = -1;
            for (int j = 1; j < n + 1; j++) {
                if(distance[j] != 0 && distance[j] < min)
                    minIndex = j;
            }
            shortest = minIndex;
        }
        return shortest;
    }
}
