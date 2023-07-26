package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private  var canAddOperation = false
    private  var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    fun NumberAction(view: View) {
        if(view is Button){
                if(view.text == "."){
                    if (canAddDecimal){
                        binding.workingstv.append(view.text)
                        canAddDecimal = false
                    }
                }else{
                    binding.workingstv.append(view.text)
                    canAddOperation = true
                }
        }
    }

    fun sciCalcAction(view: View) {
        Toast.makeText(this, "Coming Soon", Toast.LENGTH_SHORT).show()
    }

    fun operationAction(view: View) {
        if(view is Button && canAddOperation){
            binding.workingstv.append(view.text)
            canAddOperation = false
            canAddDecimal = true
        }
    }

    fun allClearAction(view: View) {
        binding.workingstv.text = ""
        binding.resultstv.text = ""
    }

    fun backspaceAction(view: View) {
        val length = binding.workingstv.length()
        if(length>0){
            binding.workingstv.text = binding.workingstv.text.subSequence(0, length-1)
        }
    }

    fun equalsAction(view: View) {
        binding.resultstv.text = calcResult()
    }

    private fun calcResult() : String {
        val digitOperators = digitsOperators()
        if(digitOperators.isEmpty()) return ""

        val timesDivision = timesDivisionCalculate(digitOperators)
        if(timesDivision.isEmpty()) return ""

        val result = addSubCalc(timesDivision)
        return result.toString()
    }

    private fun addSubCalc(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for(i in passedList.indices){
            if(passedList[i] is Char && i!= passedList.lastIndex){
                val operator = passedList[i]
                val nextDigit = passedList[i+1] as Float
                if(operator == '+'){
                    result += nextDigit
                }
                if(operator == '-'){
                    result -= nextDigit
                }
            }
        }
        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('X') || list.contains('/')){
            list = calcTimesDivision(list)
        }
        return list
    }

    private fun calcTimesDivision(passedList : MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices){
            if(passedList[i] is Char && i!= passedList.lastIndex && i<restartIndex){
                var operator = passedList[i]
                var previousDigit = passedList[i-1] as Float
                var nextDigit = passedList[i+1] as Float

                when(operator){
                    'X' ->{
                        newList.add(previousDigit*nextDigit)
                        restartIndex = i+1
                    }
                    '/' ->{
                        newList.add(previousDigit/nextDigit)
                        restartIndex = i+1
                    }
                    else ->{
                        newList.add(previousDigit)
                        newList.add(operator)
                    }
                }
            }

            if(i>restartIndex){
                newList.add(passedList[i])
            }
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any>{
        var list = mutableListOf<Any>()
        var currDigit = ""

        for(character in binding.workingstv.text){
            if(character.isDigit()|| character == '.'){
                currDigit+=character
            }else{
                list.add(currDigit.toFloat())
                currDigit = ""
                list.add(character)
            }
        }

        if(currDigit!=""){
            list.add(currDigit.toFloat())
        }
        return list
    }

}