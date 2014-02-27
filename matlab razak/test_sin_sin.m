function test_sin_sin
clc; 
x = linspace(0, 2*pi ); 
cut(2); 
cut(15); 
cut(30);  
    function cut(a)
    y = 'sin('; 
    for i=1:a,
        y = strcat(y,'sin('); 
    end
    y = strcat(y, 'x)'); 
    for i=1:a, 
        y = strcat(y, ')'); 
    end
    y = eval(y); 
    plot(x, y);
    hold on ; 
    end
end
