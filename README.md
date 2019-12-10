# Data extraction from audio
### Bài toán:
Muốn lưu dữ liệu có cấu trúc vào database bóc tách từ âm thanh, lời nói. ●	Ví dụ: "Tôi cần mua bảo hiểm một xe kia morning và hai xe Mazda CX5" -> (1, Kia, morning) (2, Mazda, CX5)

### Giải pháp sơ bộ: 
* Thử nghiệm phát triển ứng dụng trên Android 
* Dùng Google API sẵn có trên device để xử lý speech to text 
* Tìm thư viện, platform xử lý text để hiểu nghĩa và phân tách các từ.

### Tìm kiếm các giải pháp liên quan:
* Keywords:
| data extraction from audio | speech to text vietnamese| nlp vietnamese |
| 							 | 							| 				 |
| 							 | 							| 				 |

* Các vấn đề cần tham khảo:
** http://redshiftcompany.com/product-demo/
** https://cloud.google.com/community/tutorials/data-science-extraction
** https://cloud.google.com/speech-to-text/
** https://fpt.ai/stt/
** https://github.com/anhnguyen9a7/vietnamese-speech-to-text-wavenet
** https://github.com/vncorenlp/VnCoreNLP
** https://viblo.asia/p/ban-ve-xu-ly-ngon-ngu-tieng-viet-924lJYdYZPM
** https://github.com/undertheseanlp/underthesea/wiki/ 

### Giải pháp demo:
* Client Android: 
** Sử dụng library sẵn có trong SDK phần chuyển từ voice sang text
** Gọi API lên server để phân tích đoạn text
* Server Spring boot:
** Sử dụng framework Spring boot để dụng API 
** Sử dụng core Natural language processing (NLP) để phân tích text từ https://github.com/vncorenlp/VnCoreNLP 

### 4. Kết quả
**Hình minh họa**
<kbd><img title="Hình minh họa" src="https://raw.githubusercontent.com/namntdev/data-extraction/master/result.png"></kbd><br/>

**Link video**
https://youtu.be/vnWv7vRWLx4