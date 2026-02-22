#include <stdio.h>

int main() {
    int n, i, t = 0, min_index, min_remaining, completed = 0;
    int burst[20], arrival[20], remaining[20], waiting[20], turnaround[20];
    float avg_waiting = 0, avg_turnaround = 0;

    printf("Enter number of processes: ");
    scanf("%d", &n);

    for(i = 0; i < n; i++) {
        printf("Enter arrival time for process %d: ", i + 1);
        scanf("%d", &arrival[i]);
        printf("Enter burst time for process %d: ", i + 1);
        scanf("%d", &burst[i]);
        remaining[i] = burst[i];
    }

    while(completed != n) {
        min_index = -1;
        min_remaining = 9999;

        for(i = 0; i < n; i++)
            if(arrival[i] <= t && remaining[i] > 0 && remaining[i] < min_remaining)
                min_index = i, min_remaining = remaining[i];

        if(min_index == -1) {
            t++;
            continue;
        }

        remaining[min_index]--;
        t++;

        if(remaining[min_index] == 0) {
            completed++;
            turnaround[min_index] = t - arrival[min_index];
            waiting[min_index] = turnaround[min_index] - burst[min_index];
        }
    }

    printf("\nProcess\tArrival\tBurst\tWaiting\tTurnaround\n");
    for(i = 0; i < n; i++) {
        printf("P%d\t%d\t%d\t%d\t%d\n", i+1, arrival[i], burst[i], waiting[i], turnaround[i]);
        avg_waiting += waiting[i];
        avg_turnaround += turnaround[i];
    }

    printf("\nAverage Waiting Time: %.2f", avg_waiting / n);
    printf("\nAverage Turnaround Time: %.2f\n", avg_turnaround / n);

    return 0;
}
