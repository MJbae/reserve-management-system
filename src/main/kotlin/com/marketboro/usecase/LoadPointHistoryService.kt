package com.marketboro.usecase

import com.marketboro.domain.*
import com.marketboro.usecase.dto.PointHistoryDto
import com.marketboro.usecase.exceptions.MemberNotFoundException
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class LoadPointHistoryService(
    private val repository: PointAccountRepository,
    private val transRepository: PointTransactionRepository,
) {

    @Transactional(readOnly = true)
    fun loadHistory(memberId: String, pageNum: Int, pageSize: Int): PointHistoryDto {
        val pointAccount = repository.find(MemberId(memberId)) ?: throw MemberNotFoundException(MemberId(memberId))

        val transSlice = transRepository.findAll(
            pointAccount.accountId,
            types = setOf(TransactionType.USE, TransactionType.EARN),
            pageReq = PageRequest.of(pageNum, pageSize, Sort.by("createdAt").descending()),
        )

        return PointHistoryDto(transSlice)
    }
}
