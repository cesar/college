#fasm#

org  100h  

jmp start

;;;;;;;;;;;;;;;;; DECLARE VARIABLES ;;;;;;;;;;;;;;;;;; 

initial_prompt db 'Enter a string no larger than 256 characters: $'      ;Buffer for testing
                                            
sum_prompt db 'Total: $'                                                 ;Buffer for printing the result of the sum   

integer_count db 'Number of number substring: $'

num db 5 dup(?) 

ic_total db 5 dup(?)

carrRet db 13d,'$'

newLine db 10d,'$'

intResult dw 0                                                           ; Result of the addition of all integers

someOffset dw 01d                           

number_ten db 10d 

sumTotal dw 0d                                                           ; Result of addition of all integers

integerCount dw 0d                                                       ; Maintain a count of the substrings

sum_hold dw 0d                                                           ; Amount of integer substring  

user_input db 257 dup(' ')                                               ; Buffer to be used by the user  

reverse_holder db 128 dup(' ')

   
;;;;;;;;;;;;;;;;; MAIN ;;;;;;;;;;;;;;;;;;   

start:
   pusha 
    
   call read 
                                                                         ; Push all the registers
    
   mov bx, user_input                                                    ; Save the string to be searched in the base register
                                                                           
   xor cx, cx                                                            ; Clear the count register
    
for:
   cmp byte [bx], 48d                                                    ; These for lines check to see if the current character is a digit

   jl final                                                              ; If if is, call the convert2Int on that piece of string

   cmp byte [bx], 57d

   jg final                               
    
   call convert2Int                      
    
final:    

   inc bx                                                                ; Go to the next character

   cmp byte[bx], 24h                                                     ; Check to see if I'm at the last character of the string
   
   jne for  
                                                                         ; Call this function on the sumTotal 
   mov ax, [sumTotal]
    
   mov bx, num
    
   call convert2Str  
    
   mov ax, [integerCount]
    
   mov bx, ic_total
    
   call convert2Str 
   
   mov bx, user_input
   
   call reverse
    
   call write                                                            ; If not, continue looping
        
   popa                                                                  ; Pop all registers back to their original place.
                                            
ret

;;;;;;;;;;;;;;;;; CONVERT2INT PROCEDURE ;;;;;;;;;;;;;;;;;;

convert2Int:                                                             ; Function to convert a piece of string into their integer equivalents
    
    xor cx, cx                                                           ; Sanitize the registers that will be used.
    
    xor ax, ax                           
    
    xor dx, dx
    
start_ci:     
    
    cmp byte [bx], 48d                                                   ; Check to see if we've reached the end of the integer sequence. 
                                                                           
    jl final_ci
    
    cmp byte [bx], 57d
    
    jg final_ci
    
    push word [bx]                                                       ; Here we push the next word also, this has to be taken into consideration
    
    inc bx                                                               ; Increase one, since characters are byte sized.
    
    inc cx
    
    jmp start_ci 

final_ci: 
    
    pop ax                                                               ; Pop the stack (16 bits are being poped)
                                                                            
    mov ah, 0                                                            ; Eliminate the byte that we don't need
    
    sub al, 48d                                                          ; Change from ascii to an actual number
    
    mul [someOffset]                                                     ; This offset takes into consideration the fact that the number is being poped in reverse
    
    add [sum_hold], ax                                                   ; Put the number in the accumulator
    
    mov ax, [someOffset]                                                 ; Increase the offset for the next time
    
    mul [number_ten]                   
    
    mov [someOffset], ax
    
    loop final_ci                                                        ; Continue looping until the whole nomber has being popped.
    
    mov [someOffset], 01d                                                ; Reset the offset to be used the next time.
    
    dec bx
    
    mov dx, [sum_hold] 
    
    mov [sum_hold], 0d
    
    call sum                                                             ; Keep a total of the numbers.
    
    call search_count                                                    ; Keep a count of the amount of substrings.
    
    ret    


;;;;;;;;;;;;;;;;; SUM PROCEDURE ;;;;;;;;;;;;;;;;;;

sum:
    
    mov ax, 0d                                                           ; Add the last integer found to the total sum
    
    mov ax, [sumTotal]
    
    add ax, dx
    
    mov [sumTotal], ax 

    ret

search_count:
    
    push ax
    
    mov ax, [integerCount]
    
    inc ax
    
    mov [integerCount], ax
    
    pop ax                                                               ; Increment the number of integer substrings found

    ret          
    
;;;;;;;;;;;;;;;;; CONVERT2STR PROCEDURE ;;;;;;;;;;;;;;;;;;

convert2Str:                                                             ; Function that converts an integer to a string.
    
    xor dx, dx                                                           ; prepare the registers that are going to be used.
    
    xor cx, cx 
    
    mov cx, 0ah
    
    push 40h
    
start_cs:
    
    div cx                                                               ; Divide the number by it's base.
    
    push dx                                                              ; Push 16 bit contents into the stack
    
    mov dx, 0d
                                                                         ; Remove the remainder part    
    cmp ax, 0d                                                           ; Continue while the quotient is not 0
    
    jne start_cs 
    
final_cs:
    
    pop ax                                                               ; Get the number back from the stack in reverse order.
    
    cmp ax, 40h
    
    je end_cs  
    
    add al, 30h
    
    mov byte [bx], al                                                    ; Fix the number (add an offset) then add to the string.
    
    inc bx
    
    jmp final_cs 

end_cs:

    mov byte [bx], 24h                                     

    ret
    

;;;;;;;;;;;;;;;;; READ PROCEDURE ;;;;;;;;;;;;;;;;;;   

read:                                                                    ; Function that reads user input from the console.

    xor cx, cx                                                           ; Prepare the registers
    
    xor dx, dx      
    
    mov dx, initial_prompt                                            
    
    mov ah, 9
             
    int 21h
             
    mov dx, newLine
    
    mov ah, 9
    
    int 21h
    
    mov dx, carrRet
    
    mov ah, 9
    
    int 21h
    
    mov bx, user_input
    
    mov cx, 256 
    
    mov ah, 01h 
    
start_reading:
    
    int 21h
    
    cmp al, 0dh
    
    je finish_reading 
    
    mov byte [bx], al
    
    inc bx
    
    loop start_reading

finish_reading:
    
    mov byte [bx], 24h
           
    ret 
    
                               
                                  
;;;;;;;;;;;;;;;;; WRITE PROCEDURE ;;;;;;;;;;;;;;;;;;

write:
    
    mov dx, newLine
    
    mov ah, 9
    
    int 21h 
    
    int 21h
    
    mov dx, carrRet
    
    int 21h
    
    mov dx, user_input
    
    int 21h
    
    mov dx, newLine
    
    int 21h 
    
    int 21h
    
    mov dx, carrRet
    
    int 21h  
    
    mov dx, integer_count
    
    int 21h
    
    mov dx, ic_total
    
    int 21h
    
    mov dx, newLine
    
    int 21h 
    
    int 21h
    
    mov dx, carrRet
    
    int 21h
    
    mov dx, sum_prompt
    
    int 21h 
    
    mov dx, num
    
    int 21h
    
    
    
    ret


;;;;;;;;;;;;;;;;; REVERSE PROCEDURE ;;;;;;;;;;;;;;;;;;    
reverse:
    
    mov bx, user_input
    
    
reverse_start:
   
    cmp byte [bx], 30h
   
    jl reverse_final
   
    cmp byte [bx], 39h
    
    jg reverse_final
    
    call reverse_helper
    
    
reverse_final:
    
    inc bx 
    
    cmp byte [bx], 24h
    
    jne reverse_start
    
ret


;;;;;;;;;;;;;;;;; REVERSE HELPER FUNCTION ;;;;;;;;;;;;;;;;;; 
reverse_helper:
    
    xor cx, cx
  
    
    push 24h    
    
    start_rh:
        cmp byte [bx], 30h
       
        jl final_rh
       
        cmp byte [bx], 39h
       
        jg final_rh
        
        push word [bx]
       
        inc bx
       
        inc cx
       
        jmp start_rh 
        
        
    
    final_rh: 
        
         sub bx, cx      ;Return to the start to the digit substring
    
    final_rhe:     
         
         pop ax
         
         cmp ax, 24h
         
         je end_helper
         
         mov ah, 0h
         
         mov byte [bx], al
         
         inc bx
         
         jmp final_rhe
    
    end_helper:
         
         dec bx
         
    ret   