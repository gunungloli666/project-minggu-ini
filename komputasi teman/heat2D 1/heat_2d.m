clc;
close all;
clear all;
N = 51;  
DX=0.1;
DY=0.1;
Nx=5; 
Ny=5;
X=0:DX:Nx;
Y=0:DY:Ny;
alpha=5; 
U(1:N,1:N) = 0 ;
U(1,1:N) = 100; 
U(N,1:N) = 0;  
U(1:N,1) = 0;  
U(1:N,N) = 0;  
Umax=max(max(U));
DT = DX^2/(2*alpha);
M=3000;
fram=0;
Ncount=0;
loop=1;
fig_name = 0;
while loop==1;
   ERR=0; 
   U_old = U;
for i = 2:N-1
for j = 2:N-1
  Residue=(DT*((U_old(i+1,j)-2*U_old(i,j)+U_old(i-1,j))/DX^2 ... 
                      + (U_old(i,j+1)-2*U_old(i,j)+U_old(i,j-1))/DY^2) ...
                      + U_old(i,j))-U(i,j);
  ERR=ERR+abs(Residue);
  U(i,j)=U(i,j)+Residue;
end
end
if(ERR>=0.01*Umax) % batas eror untuk iterasi
         if (mod(Ncount,200)==0 || Ncount == 0) % plot tiap 200  iterasi
             fram=fram+1;
             surf(U);
             axis([1 N 1 N ])
             h=gca; 
             set(h,'FontSize',12)
             colorbar('location','eastoutside','fontsize',12);
             xlabel('X','fontSize',12);
             ylabel('Y','fontSize',12);
             title('Heat Diffusion','fontsize',12);
             fh = figure(1);
             set(fh, 'color', 'white'); 
             eval(['saveas(fh,','''gambar_',num2str(fig_name),... 
                 '''', ',''jpg''' ,');'])
             fig_name = fig_name + 1; 
             drawnow; 
         end
    if(Ncount>M) % iterasi sudah melewati batas toleransi
        loop=0;
    end
      Ncount=Ncount+1;
else % keadaan setimbang 
    loop=0;
end
end



