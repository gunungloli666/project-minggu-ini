function gravity()
h1 = 100; %dalam satuan meter
h2 = 150; %dalam satuan meter
G = 6.67*(10^(-11));%konstanta gravitasi(N.m^-2.kg^-2)
rho = 15600; %rapat massa(kg.m^(-3))
x2 = (-50:5:50);%dalam satuan meter
x1 = (-20:5:75);%dalam satuan meter

zz = length(x1);
deltaG = zeros(1 , zz);

for i=1:zz
    deltaG(1,i) = 2*rho*G*(h2*getPsi(h2 , x2(1,i)) - h1*getPsi(h1,x1(1,i))+...
    ((x2(1,i)*h1-x1(1,i)*h2)/(getDelta(x2(1,i),x1(1,i))^2+getDelta(h2, h1)^2))*(getDelta(h2,h1)...
     *log(getR(x2(1,i),h2)/getR(x1(1,i),h1))+getDelta(x2(1,i),x1(1,i))*(getPsi(h1,x1(1,i))-getPsi(h2,x2(1,i)))))  ;  
end
disp([' X1  ','   x2   ', '   delta g   ']);
for i =1:zz
    disp([num2str(x1(1,i)), '  |  ',num2str(x2(1,i)),'  |  ' , num2str(deltaG(1,i))]);
end
plot(x1,deltaG, x1, deltaG , 'ro');
end

function [hasil] = getR(x,h)
hasil = sqrt(x^2 + h^2);
end

function [hasil] =  getDelta(a2, a1)
hasil = a2 - a1;
end

function [hasil] =  getPsi(h,x)
hasil  = atan(h/x);
end