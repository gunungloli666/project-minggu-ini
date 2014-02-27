% jawaban soal nomor 57 BAB 4.7 dari buku James Stewart kalkulus
% edisi 5
% author: Mohammad Fajar
function soal57
clc; 
f = figure('menubar', 'none', 'resize', 'off', ... 
    'position', [200, 100, 800, 450]); 
ax1 = axes('units', 'pix', 'position', [50 50 340 270]... 
   ,  'xlim', [0 2*pi], 'ylim', [-1.5, 1.5]); 

ax2 = axes('units', 'pix', 'position', [430 50 340 270]... 
   ,  'xlim', [0 2*pi], 'ylim', [-1.5, 1.5]); 

slider =  uicontrol('style', 'slider', 'position', [50 380 90 20]); 
text_ = uicontrol('style', 'text', 'position', [150 380 40 20 ]); 

set(slider, 'value', 0.1);
set(slider, 'sliderstep', [0.05 0.05]);
ss = 0:0.01:3; 
theta = 180 - atand(2./ss) - atand(5./(3- ss));
maksimum = max(theta); % jawabannya
while ishandle(f)
    xx = get(slider , 'value');
    xx = xx * 3;
    set(text_, 'string', num2str(xx)); 
    trigx = [0 0;5 2;0 0]; 
    trigy = [0 3-xx;0 3;3-xx 3];
    z = [1 1;1 1;1 1];
    p = patch(trigx, trigy, z, 'parent', ax2); 
    axis([-1 ,6, -1, 4]);
    thetax = 180 - atand(2./xx) - atand(5./(3 - xx)); 
    p2 = plot(ss, theta, 'parent', ax1);
    xlabel(ax1, 'x');
    ylabel(ax1, '\theta'); 
    hold on; 
    p3 = line([xx xx], [0 thetax], 'linewidth', 4, 'parent', ax1);
    tt = text(xx+.1,thetax-3,['\theta = ', num2str(thetax)],'parent', ax1 ...
        , 'fontsize', 9);
    tt2 = text(0.8,3-xx,['\leftarrow \theta = ', num2str(thetax), '^o'],'parent', ax2 ...
        , 'fontsize', 12);
    % ini hanya untuk mengakali karena step slider dan step linspace pada 
    % MATLAB tidak sama :)
    if floor(thetax) == floor(maksimum)
        set(p3, 'color', 'r');
        set(p, 'facecolor', 'y'); 
    end
    drawnow;
    if ishandle(p), delete(p);end
    if ishandle(p2), delete(p2);end
    if ishandle(p3), delete(p3);end
    if ishandle(tt), delete(tt);end
    if ishandle(tt2), delete(tt2);end
end