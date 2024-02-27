# 📝 Projenin Spring Security kısmı part -1
### 🎯 Bu projede yazılımsal deneyimlerimi paylaşabileceğim kendime ait bir web blog sayfası yazmaya karar verdim. Projenin ilk aşaması olan üyelik işlemlerini tamamlamış bulunmaktayım.

### 👉 Kayıt olma (/register )
Üyelik işlemlerini tamamladığınızda size bir reflesh token ile access token dönüyor. Ayrıca girmiş olduğunuz mail hesabınıza da üyeliğiinizi aktif etmek için link gönderiyor. Linke tıklamak yeterli. Üyeliğiniz aktif oluyor.
### 👉 Oturum açma (/login )
Email ve şifre giriyosunuz eğer üyeliğiniz aktif edilmişse sisteme giriş yapabiliyosunuz aksi taktirde bununla ilgili uyuarı veriyor. Ayrıca giriş başarılıysa bir access token ve refresh token dönüyor. Register işleminde sahip olduğumuz tokenlar ise geçersiz oluyor.
### 👉 Token yenileme (/refresh-token )
Oturum açıkkken devam etmek istiyorsak buraya istek atıyoruz. İstek neticesinde bize yeni bir access token ve refresh token dönüyor.
### 👉 Çıkış işlemi (/logout )
Çıkış için istekte bulunduktan sonra tüm tokenlarımız geçersiz oluyor.
### 👉 Şifre Değiştirme (/change-password )
Burada parametre almadım.Token ile isteğimizi atıyoruz. Önce bir securityContexUtil sınıfı açtım. Burada Kimlik bilgilerimizi doğruluyoruz. Bize bir email döndürüyor. Bunuda changePassword methodunad kullanıyoruz. Eski şifreyi ve yeni şifremizi giriyoruz :)
### 👉 Şifremi unuttum (/forget/password )
Şifreyi unuttuysak, mail adresimizi yazıyoruz, forgetpassword methoduna istek attıkdan sonra mail adresimize link şeklinde gelen reset-password methoduna istek atmamızı sağlayan linke tıklıyoruz.
### 👉 Şifre Resetleme (/reset-password )
Burada linkten gelen token bilgisi, ve yeni şifremiz alınarak resetleme gerçekleştirilir. Hemen ardındanda bu link geçersiz olur. Kullanıcının tekrardan login olması gerekir. 

### 🎯 Ayrıca bu projede ek olarak hiç bilmediğim OpenApi entegrasyonunu sağlamayı öğrendim. Bununla ilgili küçük detayları keşfettim. İlgili bir resim buraya bırakıyorum.
![](https://r.resimlink.com/s6E1bi.png)
