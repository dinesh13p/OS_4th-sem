#include <windows.h>
#include <stdio.h>

int main() {
    STARTUPINFO si;
    PROCESS_INFORMATION pi;

    ZeroMemory(&si, sizeof(si));
    si.cb = sizeof(si);
    ZeroMemory(&pi, sizeof(pi));

    // Create a new process (this same program again)
    if (CreateProcess(
        NULL,                 // Application name
        "process.exe",        // Run this same program again (child)
        NULL, NULL, FALSE,
        0, NULL, NULL,
        &si, &pi)
    ) {
        // Parent process
        printf("Process created with ID: %lu\n", GetCurrentProcessId());

        // Wait for child to finish (optional, for clean output)
        WaitForSingleObject(pi.hProcess, INFINITE);

        // Close handles
        CloseHandle(pi.hProcess);
        CloseHandle(pi.hThread);
    } else {
        // Child process also prints here
        printf("Process created with ID: %lu\n", GetCurrentProcessId());
    }

    return 0;
}
