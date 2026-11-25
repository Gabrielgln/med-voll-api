package med.voll.api.validator;

import lombok.RequiredArgsConstructor;
import med.voll.api.domain.consulta.DadosAgendamentoConsulta;
import med.voll.api.exception.ValidacaoException;
import med.voll.api.repository.ConsultaRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MedicoComOutraConsultaNoMesmoHorarioValidator implements ConsultaValidator{
    private final ConsultaRepository consultaRepository;

    public void validar(DadosAgendamentoConsulta dados){
        var medicoPossuiOutraConsultaNoMesmoHorario = consultaRepository.existsByMedicoIdAndData(dados.idMedico(), dados.data());
        if(medicoPossuiOutraConsultaNoMesmoHorario){
            throw new ValidacaoException("Médico já possuí outra consulta agendada nesse mesmo horário.");
        }
    }
}
