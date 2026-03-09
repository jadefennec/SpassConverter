#!/usr/bin/env python3
"""Create minimal valid .spass file. Run: pip install pycryptodome && python create_test_spass.py"""
import base64
import os
try:
    from Cryptodome.Cipher import AES
    from Cryptodome.Protocol.KDF import PBKDF2
    from Cryptodome.Hash import SHA256
except ImportError:
    from Crypto.Cipher import AES
    from Crypto.Protocol.KDF import PBKDF2
    from Crypto.Hash import SHA256

SALT_LEN, IV_LEN, ITERATIONS = 20, 16, 70_000
plain = ("Samsung Pass\ntrue;false;false;false\nnext_table\norigin_url;username_value;password_value;credential_memo;title;app_name;package_name\n;aGVsbG8=;dGVzdA==;;;;;\nnext_table\nnext_table\nnext_table\n").encode("utf-8")
password, salt, iv = "test123", os.urandom(SALT_LEN), os.urandom(IV_LEN)
key = PBKDF2(password.encode("utf-8"), salt, dkLen=32, count=ITERATIONS, hmac_hash_module=SHA256)
pad_len = 16 - (len(plain) % 16)
plain += bytes([pad_len] * pad_len)
encrypted = AES.new(key, AES.MODE_CBC, iv).encrypt(plain)
out_path = os.path.join(os.path.dirname(__file__), "test_export.spass")
with open(out_path, "w") as f:
    f.write(base64.b64encode(salt + iv + encrypted).decode("ascii"))
print("Created test_export.spass | password: test123")
