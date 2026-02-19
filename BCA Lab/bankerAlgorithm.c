#include <stdio.h>

int main() {
    int n = 5, m = 3, i, j, k;

    // Sample Allocation Matrix
    int alloc[5][3] = {
        {0, 1, 0},  // P0
        {2, 0, 0},  // P1
        {3, 0, 2},  // P2
        {2, 1, 1},  // P3
        {0, 0, 2}   // P4
    };

    // Sample Maximum Matrix
    int max[5][3] = {
        {7, 5, 3},  // P0
        {3, 2, 2},  // P1
        {9, 0, 2},  // P2
        {2, 2, 2},  // P3
        {4, 3, 3}   // P4
    };

    // Available resources
    int avail[3] = {3, 3, 2};

    int need[5][3], finish[5] = {0}, safe[5], safe_index = 0;

    // Calculate Need Matrix
    for(i = 0; i < n; i++) {
        for(j = 0; j < m; j++) {
            need[i][j] = max[i][j] - alloc[i][j];
        }
    }

    // Safety Algorithm
    int count = 0;
    while(count < n) {
        int found = 0;
        for(i = 0; i < n; i++) {
            if(finish[i] == 0) {
                int can_allocate = 1;
                for(j = 0; j < m; j++) {
                    if(need[i][j] > avail[j]) {
                        can_allocate = 0;
                        break;
                    }
                }
                if(can_allocate) {
                    for(k = 0; k < m; k++) {
                        avail[k] += alloc[i][k];
                    }
                    safe[safe_index++] = i;
                    finish[i] = 1;
                    found = 1;
                    count++;
                }
            }
        }
        if(!found) {
            printf("\nSystem is not in a safe state.\n");
            return 0;
        }
    }

    // Print Safe Sequence
    printf("System is in a safe state.\nSafe sequence: ");
    for(i = 0; i < n; i++) {
        printf("P%d ", safe[i]);
    }
    printf("\n");

    return 0;
}
