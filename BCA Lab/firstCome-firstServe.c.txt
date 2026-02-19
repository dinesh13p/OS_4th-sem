#include <stdio.h>

int main() {
    int n, i;
    int burst[20], wait[20], tat[20];
    float avg_wait = 0, avg_tat = 0;

    printf("Enter number of processes: ");
    scanf("%d", &n);

    printf("Enter burst time for each process:\n");
    for (i = 0; i < n; i++) {
        printf("P%d: ", i + 1);
        scanf("%d", &burst[i]);
    }

    // First process has 0 waiting time
    wait[0] = 0;

    // Calculate waiting time
    for (i = 1; i < n; i++) {
        wait[i] = wait[i - 1] + burst[i - 1];
    }

    // Calculate turnaround time
    for (i = 0; i < n; i++) {
        tat[i] = wait[i] + burst[i];
        avg_wait += wait[i];
        avg_tat += tat[i];
    }

    // Display results
    printf("\nProcess\tBurst Time\tWaiting Time\tTurnaround Time\n");
    for (i = 0; i < n; i++) {
        printf("P%d\t%d\t\t%d\t\t%d\n", i + 1, burst[i], wait[i], tat[i]);
    }

    printf("\nAverage Waiting Time = %.2f", avg_wait / n);
    printf("\nAverage Turnaround Time = %.2f\n", avg_tat / n);

    return 0;
}
