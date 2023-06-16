# Необходимо реализовать метод разворота связного списка (двухсвязного или односвязного на выбор).

class Node:
    next = prev = None
 
    def __init__(self, data):
        self.data = data
 
def push(head, key):
 
    node = Node(key)
    node.next = head
     
    if head:
        head.prev = node
     
    head = node
    return head
 
def printDDL(msg, head):
 
    print(msg, end='')
    while head:
        print(head.data, end=' ')
        head = head.next
    print('')
 
def swap(node):
 
    prev = node.prev
    node.prev = node.next
    node.next = prev
 
def reverse(head, curr):
     
    if curr.next is None:
         
        swap(curr)
 
        head = curr
        return head
     
    swap(curr)
     
    head = reverse(head, curr.prev)
    return head

def reverseDDL(head):
 
    if not head:
        return head
 
    curr = head
 
    head = reverse(head, curr)
    return head
 
if __name__ == '__main__':
 
    head = None
    for key in range(1, 13):
        head = push(head, key)
 
    printDDL('Исходный список: ', head)
    head = reverseDDL(head)
    printDDL('Рекурсивный список: ', head)
