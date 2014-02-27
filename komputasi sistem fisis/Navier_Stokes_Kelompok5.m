%======================================================================
function Navier_Stokes_Kelompok5
%======================================================================
clear   % Bersihkan Workspace
clc     % Bersihkan Command Windows
% Program ini digunakan untuk memecahkan persamaan incompressible 
% Navier-Stokes dengan domain ruang berbentuk kotak dengan memberikan
% kecepatan pada bidang batas. 
% vektor kecepatan ditunjukan oleh tanda panah kecil
% tekanan ditunjukan oleh warna latar
% streamline (lintasan partikel fluida) digambarkan oleh garis memutar
% permasalahan: tutup kotak bergerak ke kanan
%======================================================================
Re = 100;    % Bilangan Reynolds 
dt = 0.02;   % Selang waktu
tf = 8;      % Waktu akhir simulasi
lx = 1;      % Lebar kotak domain
ly = 1;      % Tinggi kotak domain
nx = 100;    % Jumlah grid pada sumbu-x
ny = 100;    % Jumlah grid pada sumbu-y
nsteps = 10; % Jumlah tampilan keluar untuk mengetahui perubahan
%======================================================================
nt = ceil(tf/dt); 
dt = tf/nt;
x = linspace(0,lx,nx+1);
y = linspace(0,ly,ny+1); 
hx = lx/nx;               % Lebar satu grid
hy = ly/ny;               % Tinggi satu grid
%======================================================================
% KONDISI AWAL
%======================================================================
U = zeros(nx-1,ny); % Siapkan matriks untuk vektor kecepatan U
V = zeros(nx,ny-1); % Siapkan matriks untuk vektor kecepatan V
%======================================================================
% KONDISI PADA SYARAT BATAS
%======================================================================
uN = x*0+1;     %N=north (utara/arah ke kanan)
vN = avg(x)*0;  
uS = x*0; 
vS = avg(x)*0;
uW = avg(y)*0;
vW = y*0;
uE = avg(y)*0;
vE = y*0;
%======================================================================
Ubc = dt/Re*([2*uS(2:end-1)' zeros(nx-1,ny-2) 2*uN(2:end-1)']/hx^2+...
[uW;zeros(nx-3,ny);uE]/hy^2);
Vbc = dt/Re*([vS' zeros(nx,ny-3) vN']/hx^2+...
[2*vW(2:end-1);zeros(nx-2,ny-1);2*vE(2:end-1)]/hy^2);
fprintf('LOADINGGGGG')
 
Lp = kron(speye(ny),K1(nx,hx,1))+kron(K1(ny,hy,1),speye(nx));
%kron=kronecer tensor
Lp(1,1) = 3/2*Lp(1,1);
 
perp = symamd(Lp); 
Rp = chol(Lp(perp,perp)); 
Rpt = Rp';
Lu = speye((nx-1)*ny)+dt/Re*(kron(speye(ny),K1(nx-1,hx,2))+...
kron(K1(ny,hy,3),speye(nx-1)));
peru = symamd(Lu); 
Ru = chol(Lu(peru,peru));
Rut = Ru';
 
Lv = speye(nx*(ny-1))+dt/Re*(kron(speye(ny-1),K1(nx,hx,3))+...
kron(K1(ny-1,hy,2),speye(nx)));
perv = symamd(Lv);
Rv = chol(Lv(perv,perv));
Rvt = Rv';
Lq = kron(speye(ny-1),K1(nx-1,hx,2))+kron(K1(ny-1,hy,2),speye(nx-1));
perq = symamd(Lq);
Rq = chol(Lq(perq,perq));
Rqt = Rq';
fprintf(', time loop\n--20%%--40%%--60%%--80%%-100%%\n')
for k = 1:nt
%======================================================================
% PEMECAHAN SUKU NON-LINIER
%======================================================================
gamma = min(1.2*dt*max(max(max(abs(U)))/hx,max(max(abs(V)))/hy),1);
Ue = [uW;U;uE]; 
Ue = [2*uS'-Ue(:,1) Ue 2*uN'-Ue(:,end)];
Ve = [vS' V vN'];
Ve = [2*vW-Ve(1,:);Ve;2*vE-Ve(end,:)];
Ua = avg(Ue')'; 
Ud = diff(Ue')'/2;
Va = avg(Ve);
Vd = diff(Ve)/2;
UVx = diff(Ua.*Va-gamma*abs(Ua).*Vd)/hx;
UVy = diff((Ua.*Va-gamma*Ud.*abs(Va))')'/hy;
Ua = avg(Ue(:,2:end-1)); 
Ud = diff(Ue(:,2:end-1))/2;
Va = avg(Ve(2:end-1,:)')'; 
Vd = diff(Ve(2:end-1,:)')'/2;
U2x = diff(Ua.^2-gamma*abs(Ua).*Ud)/hx;
V2y = diff((Va.^2-gamma*abs(Va).*Vd)')'/hy;
U = U-dt*(UVy(2:end-1,:)+U2x);
V = V-dt*(UVx(:,2:end-1)+V2y);
%======================================================================
% VISKOSITAS
%======================================================================
rhs = reshape(U+Ubc,[],1);
u(peru) = Ru\(Rut\rhs(peru));
U = reshape(u,nx-1,ny);
rhs = reshape(V+Vbc,[],1);
v(perv) = Rv\(Rvt\rhs(perv));
V = reshape(v,nx,ny-1);
%======================================================================
% KOREKSI TEKANAN
%======================================================================
rhs = reshape(diff([uW;U;uE])/hx+diff([vS' V vN']')'/hy,[],1);
p(perp) = -Rp\(Rpt\rhs(perp));
P = reshape(p,nx,ny);
U = U-diff(P)/hx;
V = V-diff(P')'/hy;
%======================================================================
% VISUALISASI
%======================================================================
if floor(25*k/nt)>floor(25*(k-1)/nt)
    fprintf('*') 
end
 
if k==1||floor(nsteps*k/nt)>floor(nsteps*(k-1)/nt)
%======================================================================
% FUNGSI STREAM
%======================================================================
rhs = reshape(diff(U')'/hy-diff(V)/hx,[],1);
q(perq) = Rq\(Rqt\rhs(perq));
Q = zeros(nx+1,ny+1);
Q(2:end-1,2:end-1) = reshape(q,nx-1,ny-1);
 
clf %Bersihkan gambar sebelumnya
contourf(avg(x),avg(y),P',20,'w-')
hold on
contour(x,y,Q',20,'k-');
Ue = [uS' avg([uW;U;uE]')' uN'];
Ve = [vW;avg([vS' V vN']);vE];
Len = sqrt(Ue.^2+Ve.^2+eps);
quiver(x,y,(Ue./Len)',(Ve./Len)',.4,'k-')
hold off
axis equal
axis([0 lx 0 ly])
p = sort(p);
caxis(p([8 end-7]))
title(sprintf('JumGrid=%0.1g,Bilangan Reynold = %0.2g,waktu =%0.3g',...
    nx*ny,Re,k*dt))
xlabel('X')
ylabel('Y')
drawnow
end
end
fprintf('\n')
%======================================================================
function B = avg(A,k)
if nargin<2 
    k = 1;
end
if size(A,1)==1
    A = A'; 
end
if k<2 
       B = (A(2:end,:)+A(1:end-1,:))/2;
  else B = avg(A,k-1);
end
if size(A,2)==1
    B = B';
end
%======================================================================
function A = K1(n,h,a11)
%Neumann=1, Dirichlet=2, Dirichlet mid=3;
A = spdiags([-1 a11 0;ones(n-2,1)*[-1 2 -1];0 a11 -1],-1:1,n,n)'/h^2;
%======================================================================
