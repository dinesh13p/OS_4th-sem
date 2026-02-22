#include <stdio.h>

int main() {
    int n = 5, m = 3, i, j, k, count = 0, deadlock = 0, safe_index = 0, found;
    int alloc[5][3] = {{0, 1, 0}, {2, 0, 0}, {3, 0, 2}, {2, 1, 1}, {0, 0, 2}};
    int request[5][3] = {{0, 0, 0}, {2, 0, 2}, {0, 0, 0}, {1, 0, 0}, {0, 0, 2}};
    int avail[3] = {0, 0, 0};
    int finish[5] = {0}, safe[5];
    
    while(count < n) {
        found = 0;
        for(i = 0; i < n; i++) {
            if(finish[i] == 0) {
                int can_allocate = 1;
                for(j = 0; j < m; j++)
                    if(request[i][j] > avail[j]) {
                        can_allocate = 0;
                        break;
                    }
                if(can_allocate) {
                    for(k = 0; k < m; k++)
                        avail[k] += alloc[i][k];
                    finish[i] = 1;
                    safe[safe_index++] = i;
                    found = 1;
                    count++;
                }
            }
        }
        if(!found) break;
    }
    
    printf("\nProcesses in deadlock (if any): ");
    for(i = 0; i < n; i++)
        if(finish[i] == 0) {
            printf("P%d ", i);
            deadlock = 1;
        }
    printf(!deadlock ? " No deadlock detected.\n" : "\n");
    return 0;
}
