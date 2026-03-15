package com.example.kakuro_hero.services

import com.example.kakuro_hero.models.Difficulty
import com.example.kakuro_hero.models.KakuroTemplate

/**
 * Provides layout templates for Kakuro puzzles.
 */
interface IKakuroTemplateFactory {
    fun createTemplates(size: Int, difficulty: Difficulty): List<KakuroTemplate>
}