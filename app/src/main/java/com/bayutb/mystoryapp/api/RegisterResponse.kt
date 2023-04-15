package com.bayutb.mystoryapp.api

import com.google.gson.annotations.SerializedName

data class RegisterResponse(
	@field:SerializedName("error")
	val ifError: Boolean,

	@field:SerializedName("message")
	val message: String
)
