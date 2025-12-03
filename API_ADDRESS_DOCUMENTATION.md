# Tài Liệu API - Address Book (Sổ Địa Chỉ)

## Tổng Quan

API Address Book cung cấp các chức năng quản lý địa chỉ cho người dùng đã đăng nhập trong hệ thống Social Marketing.
- **Storefront API** (`/storefront/addresses`): Dành cho người dùng cuối quản lý địa chỉ của họ.

---

## 📋 Mục Lục

1. [Models](#models)
2. [Storefront API](#storefront-api)
3. [Error Handling](#error-handling)

---

## Models

### AddressResponse

Response model chi tiết cho địa chỉ:

```json
{
  "id": 1,
  "contactName": "Nguyen Van A",
  "phone": "0901234567",
  "addressLine1": "123 Nguyen Hue Street",
  "addressLine2": "Apartment 5B",
  "city": "Ho Chi Minh",
  "state": "Ho Chi Minh",
  "zipCode": "700000",
  "country": "Vietnam",
  "isDefault": true
}
```

### CreateAddressRequest

Request body để tạo địa chỉ mới:

```json
{
  "contactName": "Nguyen Van A",
  "phone": "0901234567",
  "addressLine1": "123 Nguyen Hue Street",
  "addressLine2": "Apartment 5B",
  "city": "Ho Chi Minh",
  "state": "Ho Chi Minh",
  "zipCode": "700000",
  "country": "Vietnam",
  "isDefault": true
}
```

**Lưu ý:**
- `contactName`, `phone`, `addressLine1`, `city`, `country` là bắt buộc.
- `addressLine2`, `state`, `zipCode` là tùy chọn.
- `isDefault`: Nếu `true`, địa chỉ này sẽ được đặt làm mặc định và các địa chỉ khác sẽ tự động bỏ cờ mặc định.

### UpdateAddressRequest

Request body để cập nhật địa chỉ (tất cả các trường đều optional):

```json
{
  "contactName": "Nguyen Van B",
  "phone": "0907654321",
  "addressLine1": "456 Le Loi Street",
  "addressLine2": null,
  "city": "Hanoi",
  "state": "Hanoi",
  "zipCode": "100000",
  "country": "Vietnam",
  "isDefault": false
}
```

---

## Storefront API

Base URL: `/storefront/addresses`

**Yêu cầu Authentication:** Tất cả các endpoints đều yêu cầu Bearer Token trong header `Authorization`.

### 1. Lấy Danh Sách Địa Chỉ

**Endpoint:** `GET /storefront/addresses`

**Mô tả:** Lấy danh sách tất cả địa chỉ của người dùng hiện tại (đăng nhập).

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** `List<AddressResponse>`

**Example Response:**
```json
[
  {
    "id": 1,
    "contactName": "Nguyen Van A",
    "phone": "0901234567",
    "addressLine1": "123 Nguyen Hue Street",
    "addressLine2": "Apartment 5B",
    "city": "Ho Chi Minh",
    "state": "Ho Chi Minh",
    "zipCode": "700000",
    "country": "Vietnam",
    "isDefault": true
  },
  {
    "id": 2,
    "contactName": "Nguyen Van B",
    "phone": "0907654321",
    "addressLine1": "456 Le Loi Street",
    "addressLine2": null,
    "city": "Hanoi",
    "state": "Hanoi",
    "zipCode": "100000",
    "country": "Vietnam",
    "isDefault": false
  }
]
```

### 2. Tạo Địa Chỉ Mới

**Endpoint:** `POST /storefront/addresses`

**Mô tả:** Tạo địa chỉ mới cho người dùng hiện tại.

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:** `CreateAddressRequest`

**Response:** `AddressResponse`

**Example Request:**
```json
{
  "contactName": "Nguyen Van C",
  "phone": "0909999999",
  "addressLine1": "789 Tran Hung Dao Street",
  "addressLine2": "Floor 3",
  "city": "Da Nang",
  "state": "Da Nang",
  "zipCode": "550000",
  "country": "Vietnam",
  "isDefault": false
}
```

**Example Response:**
```json
{
  "id": 3,
  "contactName": "Nguyen Van C",
  "phone": "0909999999",
  "addressLine1": "789 Tran Hung Dao Street",
  "addressLine2": "Floor 3",
  "city": "Da Nang",
  "state": "Da Nang",
  "zipCode": "550000",
  "country": "Vietnam",
  "isDefault": false
}
```

### 3. Cập Nhật Địa Chỉ

**Endpoint:** `PUT /storefront/addresses/{id}`

**Mô tả:** Cập nhật địa chỉ. Người dùng chỉ có thể cập nhật địa chỉ của chính họ.

**Path Parameters:**
- `id`: ID của địa chỉ cần cập nhật

**Headers:**
```
Authorization: Bearer <token>
Content-Type: application/json
```

**Body:** `UpdateAddressRequest`

**Response:** `AddressResponse`

**Example Request:**
```json
{
  "contactName": "Nguyen Van C Updated",
  "isDefault": true
}
```

**Example Response:**
```json
{
  "id": 3,
  "contactName": "Nguyen Van C Updated",
  "phone": "0909999999",
  "addressLine1": "789 Tran Hung Dao Street",
  "addressLine2": "Floor 3",
  "city": "Da Nang",
  "state": "Da Nang",
  "zipCode": "550000",
  "country": "Vietnam",
  "isDefault": true
}
```

### 4. Xóa Địa Chỉ

**Endpoint:** `DELETE /storefront/addresses/{id}`

**Mô tả:** Xóa địa chỉ. Người dùng chỉ có thể xóa địa chỉ của chính họ.

**Path Parameters:**
- `id`: ID của địa chỉ cần xóa

**Headers:**
```
Authorization: Bearer <token>
```

**Response:** `200 OK` (no content)

---

## Error Handling

- `400 Bad Request`: Dữ liệu đầu vào không hợp lệ (thiếu trường bắt buộc, format không đúng...).
- `401 Unauthorized`: Người dùng chưa đăng nhập hoặc token không hợp lệ.
- `404 Not Found`: Không tìm thấy địa chỉ hoặc địa chỉ không thuộc về người dùng hiện tại.
- `500 Internal Server Error`: Lỗi hệ thống.

---

## Lưu Ý Quan Trọng

1. **Bảo mật**: Tất cả endpoints tự động lấy thông tin người dùng từ token xác thực. Người dùng chỉ có thể xem/sửa/xóa địa chỉ của chính họ.

2. **Địa chỉ mặc định**: Mỗi người dùng chỉ có thể có một địa chỉ mặc định. Khi đặt một địa chỉ thành mặc định, các địa chỉ khác tự động bỏ cờ mặc định.

3. **Validation**: Các trường `contactName`, `phone`, `addressLine1`, `city`, `country` là bắt buộc khi tạo địa chỉ mới.
