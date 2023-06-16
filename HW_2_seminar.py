# Реализовать алгоритм пирамидальной сортировки (сортировка кучей).

def pyramid_sort(arr):
    arr_len = len(arr)
    for i in range(arr_len):
        for j in range(arr_len - i - 1):
            if arr[j] > arr[j + 1]:
                arr[j], arr[j + 1] = arr[j + 1], arr[j]
    print('[', end='')
    for i in range(arr_len):
        print(arr[i], end=' ' if i != arr_len - 1 else '')
    print(']')
    
arr = [11, 77, 44, 99, 55, 33]
pyramid_sort(arr) 
