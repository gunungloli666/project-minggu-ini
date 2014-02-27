function garis_singgung
% author: mohammad fajar :)
clc; 
f = figure('menubar', 'none', 'resize', 'off'); 
axes('units', 'pix', 'position', [50 50 450 300]... 
   ,  'xlim', [0 2*pi], 'ylim', [-1.5, 1.5]); 
slider =  uicontrol('style', 'slider', 'position', [50 380 90 20]); 
text = uicontrol('style', 'text', 'position', [150 380 40 20 ]); 
t = 0:.1:2*pi;  
y1 = sin(t);  
y2 = cos(t); 
plot(t, y1 , t, y2);
hold on ;
line([0 2*pi], [0 0]);
hold on ;
while ishandle(f)
    value = get(slider , 'value');
    value = value * 2 * pi; 
    set(text, 'string', num2str(value)); 
    hasil = cos(value); 
    yy = (t - value ).* hasil + sin(value) ;
    p = plot(t, yy, 'r' , 'linewidth', 2); 
    p1 = line([value value ] , [0 cos(value)], 'linewidth', 4);
    p2 = plot(value, sin(value),'r.' , 'markersize', 23); 
    % method drawnow tidak mengenali settingan di atas
    axis([0 , 2*pi, -1.5, 1.5 ]); 
    drawnow; 
    if ishandle(p), delete(p); end
    if ishandle(p1), delete(p1); end
    if ishandle(p2), delete(p2); end
end
end