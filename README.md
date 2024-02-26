# 📝 Projenin Spring Security kısmı part -1
### 🎯 Bu projede yazılımsal deneyimlerimi paylaşabileceğim kendime ait bir web blog sayfası yazmaya karar verdim. Projenin ilk aşaması olan üyelik işlemlerini tamamlamış bulunmaktayım.

### 👉 Kayıt olma (/register )
üyelik işlemlerini tamamladığınızda size bir reflesh token ile access token dönüyor. Ayrıca Girmiş oldugnuz mail hesabınızada üyeliğiinizi aktif etmek için link gönderiyor. Linke tıklamak yeterli. üyeliğiniz aktif oluyor.
### 👉 Oturum açma (/login )
email ve şifre giriyosunuz eğer üyeliğiniz aktif edilmişse sisteme giriş yapabiliyosunuz.Aksi taktirde bununla ilgili uyuarı veriyor. Ayrıca giriş başarılıysa bir access token ve refresh token dönüyor. Register işleminde sahip oldgumuz tokenlar ise geçersiz oluyor.
### 👉 Token yenileme (/refresh-token )
oturum açıkkken devam etmek istiyorsak buraya istek atıyoruz. istek neticesinde bize yeni bir access token ve refresh token dönüyor.
### 👉 Çıkış işlemi (/logout )
çıkış için istekte bulunduktan sonra tüm tokenlarımız geçersiz oluyor.
### 👉 Şifre Değiştirme (/change-password )
Burada parametre almadım.Token ile isteğimizi atıyoruz. Önce bir securityContexUtil sınıfı açtım. Burada Kimlik bilgilerimizi doğruluyoruz. Bize bir email döndürüyor. Bunuda changePassword methodunad kullanıyoruz. Eski şifreyi ve yeni şifremizi giriyoruz :)
### 👉 Şifremi unuttum (/forget/password )
Şifreyi unuttuysak, mail adresimizi yazıyoruz, forgetpassword methoduna istek attıkdan sonra mail adresimize link şeklinde gelen reset-password methoduna istek atmamızı sağlayan linke tıklıyoruz.
### 👉 Şifre Resetleme (/reset-password )
Burada linkten gelen token bilgisi, ve yeni şifremiz alınarak resetleme gerçekleştirilir. Hemen ardındanda bu link geçersiz olur. Kullanıcının tekrardan login olması gerekir. 

