package ac.kr.tukorea.capstone_android.activity

import ac.kr.tukorea.capstone_android.R
import ac.kr.tukorea.capstone_android.RetrofitWork
import ac.kr.tukorea.capstone_android.data.RegisterRequestBody
import ac.kr.tukorea.capstone_android.databinding.ActivityRegisterBinding
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.View
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding : ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val retrofitWork = RetrofitWork()

        var idCheck = false
        var nicknameCheck = false
        var pwCheck = false
        var emailCheck = false
        var phoneNumberCheck = false

        binding.apply {
            /*
                회원가입 버튼 누를 때
                    1. idCheck가 false일 때
                    2. nicknameCheck가 false일 때
                    3. pwCheck가 false일 때
                    4. emailCheck가 false일 때
                    5. phoneNumber가 false일 때
                    6. 위 경우 모두가 아닐 때 회원가입 완료
             */
            btnRegister.setOnClickListener{
                if(!idCheck){
                    textIdCheck.text = "아이디 중복확인을 해주세요."
                    textIdCheck.setTextColor(Color.RED);
                    textIdCheck.visibility = View.VISIBLE;

                    Log.d("회원가입 버튼 클릭", "아이디 중복확인 클릭: $idCheck")
                } else if(!nicknameCheck){
                    textNicknameCheck.text = "닉네임 중복확인을 해주세요."
                    textNicknameCheck.setTextColor(Color.RED)
                    textNicknameCheck.visibility = View.VISIBLE
                } else if(!pwCheck){
                    textPwCheckingMessage.text = "비밀번호를 확인해주세요."
                    textPwCheckingMessage.setTextColor(Color.RED)
                    textPwCheckingMessage.visibility = View.VISIBLE
                } else if(!emailCheck){
                    textEmailCheck.text = "이메일을 확인해주세요."
                    textEmailCheck.setTextColor(Color.RED)
                    textEmailCheck.visibility = View.VISIBLE
                } else if(!phoneNumberCheck){
                    textPhoneNumberCheck.text = "핸드폰 번호를 확인해주세요."
                    textPhoneNumberCheck.setTextColor(Color.RED)
                    textPhoneNumberCheck.visibility = View.INVISIBLE
                } else{
                    val userData = RegisterRequestBody(
                        edtIdRegister.text.toString(),
                        edtPwRegister.text.toString(),
                        edtEmail.text.toString(),
                        edtPhoneNumber.text.toString(),
                        edtNickname.text.toString()
                    )
                    retrofitWork.register(userData, binding, this@RegisterActivity)
                }
            }

            /*
                ID 중복체크 버튼 누를 때
                    1. 아이디가 3글자 미만
                    2. 아이디가 중복
                    3. 아이디 사용 가능
             */
            btnIdCheckDuplication.setOnClickListener{
                val id = edtIdRegister.text.toString()

                if(id.length < 3){
                    textIdCheck.text = "아이디를 3글자 이상 입력해주세요."
                    textIdCheck.setTextColor(Color.RED);
                    textIdCheck.visibility = View.VISIBLE;

                    Log.d("ID 중복체크", "ID의 길이가 3글자 미만")
                }
                else{
                    retrofitWork.checkDuplicateId(id, binding)

                    idCheck = textIdCheck.text.equals("사용 가능한 아이디입니다.")

                    Log.d("ID 중복체크", "ID 사용가능: $idCheck")
                }
            }

            /*
                닉네임 중복체크 버튼 누를 때
                    1. 닉네임가 3글자 미만
                    2. 닉네임가 중복
                    3. 닉네임 사용 가능
             */
            btnNicknameCheckDuplication.setOnClickListener {
                val nickname = edtNickname.text.toString()

                if(nickname.length < 6){
                    textNicknameCheck.text = "닉네임을 6글자 이상 입력해주세요."
                    textNicknameCheck.setTextColor(Color.RED)
                    textNicknameCheck.visibility = View.VISIBLE

                    Log.d("닉네임 중복체크", "닉네임이 6글자 미만")
                } else{
                    retrofitWork.checkDuplicateNickname(nickname, binding)
                    nicknameCheck = textNicknameCheck.text.equals("사용 가능한 닉네임입니다.")

                    Log.d("닉네임 중복체크", "닉네임 사용가능: $nicknameCheck")
                }
            }

            /*
                비밀번호를 입력할 때
                    1. 비밀번호가 6글자 미만
                    2. 비밀번호 사용 가능
                    3. 비밀번호와 비밀번호 확인을 입력하고 비밀번호를 바꿀 때
             */
            edtPwRegister.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val pw1 = edtPwRegister.text.toString()
                    val pw2 = edtPwCheck.text.toString()

                    if(pw1.length < 6){
                        textPwCheckingMessage.text = "비밀번호를 6글자 이상 입력해주세요."
                        textPwCheckingMessage.setTextColor(Color.RED)
                        textPwCheckingMessage.visibility = View.VISIBLE

                        pwCheck = false
                    } else if(pw2.isNotEmpty() && pw1 != pw2){
                        textPwCheckingMessage.text = "비밀번호가 일치하지 않습니다."
                        textPwCheckingMessage.setTextColor(Color.RED)
                        textPwCheckingMessage.visibility = View.VISIBLE

                        pwCheck = false
                    } else if(pw2.isNotEmpty() && pw1 == pw2){
                        textPwCheckingMessage.text = "비밀번호가 일치합니다."
                        textPwCheckingMessage.setTextColor(Color.BLUE)
                        textPwCheckingMessage.visibility = View.VISIBLE

                        pwCheck = true
                    } else{
                        textPwCheckingMessage.visibility = View.INVISIBLE
                    }
                }
            })

            /*
                비밀번호 확인을 입력할 때
                    1. 비밀번호와 비밀번호 확인이 다름
                    2. 비밀번호와 비밀번호 확인이 같음
             */
            edtPwCheck.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val pw1 = edtPwRegister.text.toString()
                    val pw2 = edtPwCheck.text.toString()

                    if(pw1 == pw2){
                        textPwCheckingMessage.text = "비밀번호가 일치합니다."
                        textPwCheckingMessage.setTextColor(Color.BLUE)
                        textPwCheckingMessage.visibility = View.VISIBLE

                        pwCheck = true
                    }else{
                        textPwCheckingMessage.text = "비밀번호가 일치하지 않습니다."
                        textPwCheckingMessage.setTextColor(Color.RED)
                        textPwCheckingMessage.visibility = View.VISIBLE

                        pwCheck = false
                    }
                }
            })

            /*
                ID를 변경할 때
                    idCheck를 false로 변경
             */

            edtIdRegister.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    idCheck = false
                    textIdCheck.visibility = View.INVISIBLE
                }
            })

            /*
                닉네임을 변경할 때
                    nicknameCheck를 false로 변경
             */
            edtNickname.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    nicknameCheck = false
                    textNicknameCheck.visibility = View.INVISIBLE
                }
            })

            /*
                이메일을 입력할 때
                이메일 형식에 맞춰 입력하는지 확인
             */
            edtEmail.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val email = edtEmail.text.toString()

                    if(Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                        emailCheck = true
                        textEmailCheck.visibility = View.INVISIBLE
                    }  else{
                        emailCheck = false
                        textEmailCheck.text = "이메일 형식에 맞춰 입력해주세요."
                        textEmailCheck.setTextColor(Color.RED)
                        textEmailCheck.visibility = View.VISIBLE
                    }
                }
            })

            /*
                핸드폰 번호를 입력할 때
                핸드폰 번호 형식에 맞춰 입력하는지 확인
             */
            edtPhoneNumber.addTextChangedListener(object : TextWatcher{
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

                override fun afterTextChanged(p0: Editable?) {
                    val phoneNumber = edtPhoneNumber.text.toString()

                    if(Pattern.matches("^01(?:0|1|[6-9])\\d{7,8}", phoneNumber)){
                        phoneNumberCheck = true
                        textPhoneNumberCheck.visibility = View.INVISIBLE
                    } else{
                        phoneNumberCheck = false
                        textPhoneNumberCheck.text = "핸드폰 번호 형식에 맞춰 입력해주세요."
                        textPhoneNumberCheck.setTextColor(Color.RED)
                        textPhoneNumberCheck.visibility = View.VISIBLE
                    }
                }
            })

        }
    }
}