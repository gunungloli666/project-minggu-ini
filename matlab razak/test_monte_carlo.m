function test_monte_carlo
clear all ;
clc;
f = figure('menubar', 'none', 'resize', 'off' ); 

ax = axes('units', 'pix', 'position', [40 50 340 340]);

uicontrol('units', 'pix', 'position', [400 360 60 25],... 
    'style', 'text','string','f(x) :' ,  'fontsize', 12, 'horizontalAlignment', 'left', ...
    'fontsize', 12, 'fontweight', 'bold', 'backgroundcolor', get(f, 'color'));

% input fungsi
inputFungsi = uicontrol('units', 'pix', 'position', [450 360 100 25],... 
    'style', 'edit', 'fontsize', 12);

uicontrol('units', 'pix', 'position', [400 330 60 25],... 
    'style', 'text','string','N :' ,  'fontsize', 12, 'horizontalAlignment', 'left', ...
    'fontsize', 12, 'fontweight', 'bold', 'backgroundcolor', get(f, 'color'));

inputN = uicontrol('units', 'pix', 'position', [450 330 100 25],... 
    'style', 'edit', 'fontsize', 12, 'fontweight', 'bold');

% keterangan hasil
ket_hasil  = uicontrol('units', 'pix', 'position', [400 280 100 30],... 
    'style', 'text', 'fontsize', 12, 'fontweight', 'bold', ...
    'string', '0' , 'backgroundcolor', get(f, 'color'));

% hitung button
uicontrol('units', 'pix', 'position', [400 250 100 30],... 
    'style', 'pushbutton', 'fontsize', 12, 'fontweight', 'bold', ...
    'string', 'HITUNG', 'Callback', @fungsi_hitung);

% reset button
uicontrol('units', 'pix', 'position', [400 210 100 30],... 
    'style', 'pushbutton', 'fontsize', 12, 'fontweight', 'bold', ...
    'string', 'RESET', 'Callback', @reset_fungsi);

    function fungsi_hitung(varargin)
        N = get(inputN , 'string'); 
        N = str2double(N); 
        x = 0:.01:2*pi; 
        yy = get(inputFungsi, 'string'); 
        y = eval(yy, x); 
        plot(x,y, 'parent', ax, 'linewidth', 3);
        hold on;
        miny = min(y); 
        maxy = max(y);
        deltax = linspace( 0 , 2*pi ,10000); 
        deltay = linspace( miny,maxy, 10000); 
        a = 0;
        for i=1:N,
            m = randi(length(deltax),1);
            n = randi(length(deltay),1);
            x = deltax(m); 
            y = deltay(n);
            if  y  >= 0 
                if y  <= eval(yy, x)
                    a = a + 1;
                    plot(x,y, 'g*', 'parent', ax);
                    hold on ;
                else
                   plot(x,y, 'r*', 'parent', ax);
                   hold on ;
                end
            else
                if y  >= eval(yy, x)
                    a = a + 1;
                     plot(x,y, 'g*', 'parent', ax);
                     hold on ;
                else
                     plot(x,y, 'r*', 'parent', ax);
                     hold on ;
                end
            end
        end
        axis tight; 
        luas =  (a/N) * ( (maxy- miny) * (2*pi - 0)); 
        set(ket_hasil, 'string', num2str(luas)); 
    end
    
    function reset_fungsi(varargin)
        cla reset; 
        set(ket_hasil, 'string', '0'); 
    end
end