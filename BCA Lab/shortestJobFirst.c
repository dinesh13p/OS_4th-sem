#include <stdio.h>

int main() {
    int n, i, j;
    int burst[20], temp;
    int waiting[20], turnaround[20];
    float avg_waiting = 0, avg_turnaround = 0;

    printf("Enter number of processes: ");
    scanf("%d", &n);

    for(i = 0; i < n; i++) {
        printf("Enter burst time for process %d: ", i + 1);
        scanf("%d", &burst[i]);
    }

    // Sorting burst times (SJF)
    for(i = 0; i < n-1; i++) {
        for(j = i+1; j < n; j++) {
            if(burst[i] > burst[j]) {
                temp = burst[i];
                burst[i] = burst[j];
                burst[j] = temp;
            }
        }
    }

    // Calculating waiting times
    waiting[0] = 0;
    for(i = 1; i < n; i++) {
        waiting[i] = waiting[i-1] + burst[i-1];
    }

    // Calculating turnaround times
    for(i = 0; i < n; i++) {
        turnaround[i] = waiting[i] + burst[i];
    }

    printf("\nProcess\tBurst Time\tWaiting Time\tTurnaround Time\n");
    for(i = 0; i < n; i++) {
        printf("P%d\t%d\t\t%d\t\t%d\n", i+1, burst[i], waiting[i], turnaround[i]);
        avg_waiting += waiting[i];
        avg_turnaround += turnaround[i];
    }

    avg_waiting /= n;
    avg_turnaround /= n;

    printf("\nAverage Waiting Time: %.2f", avg_waiting);
    printf("\nAverage Turnaround Time: %.2f\n", avg_turnaround);

    return 0;
}
