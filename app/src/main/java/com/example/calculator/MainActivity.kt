package com.example.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.databinding.DataBindingUtil
import com.example.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var canAddOperation=false
    private var canAddDecimal=true
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= DataBindingUtil.setContentView(this,R.layout.activity_main)
    }

    fun numberAction(view: View){
        if (view is Button){
            if(view.text == "."){
                if(canAddDecimal){
                    binding.workingTV.append(view.text)
                }
                canAddDecimal=false
            }
            else
                binding.workingTV.append(view.text)

            canAddOperation=true
        }
    }

    fun operationAction(view: View){
        if (view is Button && canAddOperation){
            binding.workingTV.append(view.text)
            canAddOperation=false
        }
    }

    fun allClearAction(view: View){
        binding.workingTV.text=""
        binding.resultsTV.text=""
    }
    fun backSpaceAction(view: View){
        val length=binding.workingTV.length()
        if(length>0){
            binding.workingTV.text=binding.workingTV.text.subSequence(0,length-1)
        }
    }
    fun equalsAction(view: View){
        binding.resultsTV.text=calculateResults()
    }

    private fun calculateResults():String{
        val digitOperators=digitOperators()
        if(digitOperators.isEmpty()) return ""
        val timedivision=timedivisionCalculate(digitOperators)
        if (timedivision.isEmpty()) return ""

        val result = addSubtract(timedivision)
        return result.toString()
    }

    private fun addSubtract(passedList: MutableList<Any>): Float{
        var result=passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i < passedList.lastIndex){
                var operator = passedList[i]
                var nextDigit= passedList[i+1] as Float

                if(operator == '+')
                    result+=nextDigit

                if(operator == '-')
                    result-=nextDigit
            }
        }

        return result
    }

    private fun timedivisionCalculate(passedList: MutableList<Any>):MutableList<Any>{
        var list=passedList

        while(list.contains('x') || list.contains('/')){
            list=calcTimeDivison(list)
        }
        return list
    }

    private fun calcTimeDivison(passedList: MutableList<Any>): MutableList<Any> {
        val newlist= mutableListOf<Any>()
        var restartIndex=passedList.size

        for(i in passedList.indices){
            if (passedList[i] is Char && i!=passedList.lastIndex && i<restartIndex){
                val operator=passedList[i]
                val prevDigit=passedList[i-1] as Float
                val nextDigit=passedList[i+1] as Float

                when(operator){
                    'x'->
                    {
                        newlist.add(prevDigit * nextDigit)
                        restartIndex=i+1
                    }
                    '/' ->
                    {
                        newlist.add(prevDigit / nextDigit)
                        restartIndex=i+1
                    }
                    else->{
                        newlist.add(prevDigit)
                        newlist.add(operator)
                    }
                }

            }
            if(i > restartIndex){
                newlist.add(passedList[i])
            }
        }

    return newlist
    }

    private fun digitOperators():MutableList<Any>{
        val list = mutableListOf<Any>()
        var currentdigit=""

        for(character in binding.workingTV.text){
            if (character.isDigit() || character == '.'){
                currentdigit+=character
            }
            else{
                list.add(currentdigit.toFloat())
                currentdigit=""
                list.add(character)
            }
        }

        if(currentdigit!="")
            list.add(currentdigit.toFloat())

        return list
    }
}

