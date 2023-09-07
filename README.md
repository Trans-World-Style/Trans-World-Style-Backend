# Trans-World-Style-Backend

**Requirements**
*******
```

```

**Back-End Structure**
*******
![스크린샷 2023-09-07 142414](https://github.com/Trans-World-Style/Trans-World-Style-Backend/assets/124111142/4cd7dc74-8573-4c53-93ad-b3bfcab1e959)

**APIs**
*******

**1. Member API**
* provide **google login** [request - google id token]
```
https://tw-style.duckdns.org:12522/member/auth
```

**2. Video API**
* provide **video list** [request header - Authorization : {jwt}]
```
https://tw-style.duckdns.org:12523/video/list/email
```
* **file upload** to bucket(s3) and send to **AI Server** [request header - Authorization : {jwt}]
```
https://tw-style.duckdns.org:12523/video/upload
```
* **delete** video
```
https://tw-style.duckdns.org:12523/video/update/delete_state/{video_id}
```

**Spring Cloud Gateway**
*******
**1. Member Filter**
*make jwt
**2. Video Filter**
*validate jwt




