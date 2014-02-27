function hasil = semivariogram(A, i,C0,C, a,k, m)
[cc, cd] = size(A);
temp = zeros(cc,cd); 
    switch i
    %tipe bola 
    case 0
        for i=1:cc
            for j =1:cd
                if A(i, j)<=a && A(i, j) > 0
                    temp(i, j) = C0+C*(3*A(i,j)/(2*a)-A(i,j)^3/(2*a^3));
                elseif A(i,j) > a
                    temp(i,j) = C0+C;
                end
            end
        end
%     tipe exponensial
    case 1
        for i=1:cc
            for j=1:cd
                temp(i, j) = C0+C*(1-exp(-A(i,j)/a));
            end
        end
    %tipe gaussian
    case 2
        for i=1:cc
            for j=i:cd
                temp(i, j) = C0+C*(1-exp((-1)*(A(i,j)/a)^2));
            end
        end
   %tipe gelombang 
    case 3
%         temp(1:cc, 1:cd) = C0+C*(1- (a)*(sin(A(1:cc, 1:cd)./a)));
        for i=1:cc
            for j=1:cd
                if A(i,j) == 0
                    temp(i,j)  = A(i,j);
                else
                    temp(i,j) = C0 + C*(1-(a/A(i,j))*(sin(A(i,j)/a)));
                end
            end 
        end
    %tipe kuadrat
    case 4
%         try
%             temp(1:cc,1:cd) = C0 + k*A(1:cc,1:cd).^m;
%         catch
%             temp(1:cc,1:cd) = A(1:cc,1:cd);
%         end
        for i=1:cc
            for j=1:cd
                temp(i,j) = C0 + k*A(i,j)^m;
%                 disp(['test 2  ',num2str(A(i,j)),'  ' , num2str(temp(i,j))]);
            end
        end
    otherwise
        temp(1:cc, 1:cd) = A(1:cc, 1:cd);
    end
    hasil = temp;
end